package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

import General.AppRunning;
import General.Commands;

public class SendAppRunning implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket = null;
	private DataOutputStream dataOutputStream = null;
	
//	public static void main(String[] args) throws IOException {
//		SendAppRunning sendAppRunning = new SendAppRunning(null);
//		sendAppRunning.getListAppRunning();
//	}

	public SendAppRunning(Socket socket) throws IOException {
		this.setSocket(socket);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	@SuppressWarnings("deprecation")
	public LinkedList<AppRunning> getListAppRunning(){
		try {
			Process processName = Runtime.getRuntime().exec("powershell \"Get-Process "
					+ "| Where-Object { $_.MainWindowTitle -ne '' } "
					+ "| Select-Object ProcessName, Id\"");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(processName.getInputStream()));
            String line;
            
            LinkedList<AppRunning> listApp = new LinkedList<AppRunning>(); 
            
            line = reader.readLine();
            line = reader.readLine();
            line = reader.readLine();
             
            while ((line = reader.readLine()) != null) {
            	String[] tokens = line.split("\\s+");
            	if(tokens.length == 1) break;
            	String Name = "";
            	String Id = "";
            	for(int i = 0; i < tokens.length; ++i) {
            		if(i == tokens.length - 1) {
            			Id = Id + tokens[i];
            		} else {
            			Name = Name + tokens[i];
            		}
            	}
//            	System.out.println("Name: " + Name + ", Id: " + Id);
            	listApp.add(new AppRunning(Name, Integer.parseInt(Id)));
            }
            return listApp;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public void sendAppRunning() throws IOException {
		LinkedList<AppRunning> listAppRunning = this.getListAppRunning();
		dataOutputStream.writeInt(Commands.RESPONSE_APP_RUNNING.getAbbrev());
		dataOutputStream.writeInt(listAppRunning.size());
		
		for (AppRunning appRunning : listAppRunning) {
			dataOutputStream.writeUTF(appRunning.getName());
			dataOutputStream.writeInt(appRunning.getId());
		}
		
		dataOutputStream.flush();
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
