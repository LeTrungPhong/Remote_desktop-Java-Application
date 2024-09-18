package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

import General.Commands;
import General.ProcessWindow;

public class SendProcess implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Socket socket = null;
	private DataOutputStream dataOutputStream = null;
	
	public SendProcess(Socket socket) throws IOException {
		this.setSocket(socket);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	public LinkedList<ProcessWindow> getListProcess(){
		try {
            Process process = Runtime.getRuntime().exec("tasklist");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            
            LinkedList<ProcessWindow> listProcess = new LinkedList<ProcessWindow>(); 
            
            line = reader.readLine();
            line = reader.readLine();
            line = reader.readLine();
            
            int[] sizeToken = new int[5];
            
            String[] tokens = line.split("\\s+");
            for(int i = 0; i < sizeToken.length; ++i) {
            	sizeToken[i] = tokens[i].length();
            }
            
            while ((line = reader.readLine()) != null) {
            	
                int count = 0;
                
                String ImageName = line.substring(count, count + sizeToken[0]).trim();
                count = count + sizeToken[0] + 1;
                
                int PID = Integer.parseInt(line.substring(count, count + sizeToken[1]).trim());
                count = count + sizeToken[1] + 1;
                
                String SessionName = line.substring(count, count + sizeToken[2]).trim();
                count = count + sizeToken[2] + 1;
                
                int SessionIDs = Integer.parseInt(line.substring(count, count + sizeToken[3]).trim());
                count = count + sizeToken[3] + 1;
                
                String MemUsage = line.substring(count, count + sizeToken[4]).trim();
                
                System.out.println(ImageName + " " + PID + " " + SessionName + " " + SessionIDs + " " + MemUsage);
                
                listProcess.add(new ProcessWindow(ImageName, PID, SessionName, SessionIDs, MemUsage));
            }
            return listProcess;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public void sendProcess() throws IOException {
		System.out.println("Send process");
		LinkedList<ProcessWindow> list = this.getListProcess();
		dataOutputStream.writeInt(Commands.RESPONSE_PROCESS.getAbbrev());
		dataOutputStream.writeInt(list.size());
		
		for (ProcessWindow processWindow : list) {
			dataOutputStream.writeUTF(processWindow.getImageName());
			dataOutputStream.writeInt(processWindow.getPID());
			dataOutputStream.writeUTF(processWindow.getSessionName());
			dataOutputStream.writeInt(processWindow.getSessionIDs());
			dataOutputStream.writeUTF(processWindow.getMemUsage());
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
