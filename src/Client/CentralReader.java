package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.LinkedList;

import General.ProcessWindow;

public class CentralReader implements Runnable {
	
	private Client client = null;
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private ReceiveScreen receiveScreen = null;
	private RemoteForm remoteForm = null;
	private ProcessManagementForm processManagementForm = null;
	
	public CentralReader(Socket socket, Client client) throws IOException {
		this.setClient(client);
		this.setSocket(socket);
		this.setRemoteForm(client.getRemoteForm());
		processManagementForm = client.getProcessManagementForm();
		dataInputStream = new DataInputStream(socket.getInputStream());
		receiveScreen = new ReceiveScreen(socket, client);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				int type = dataInputStream.readInt();
//				System.out.println("type: " + type);
				switch (type) {
				case -13: {
					receiveScreen.receiveScreenByCommands(dataInputStream.readInt());
					break;
				}
				case -15: {
					System.out.println("Bat dau doc process...");
					LinkedList<ProcessWindow> list = new LinkedList<ProcessWindow>();
					
					int size = dataInputStream.readInt();
					
					for(int i = 0; i < size; ++i) {
						String ImageName = dataInputStream.readUTF();
						int PID = dataInputStream.readInt();
						String SessionName = dataInputStream.readUTF();
						int SessionIDs = dataInputStream.readInt();
						String MemUsage = dataInputStream.readUTF();
						list.add(new ProcessWindow(ImageName, PID, SessionName, SessionIDs, MemUsage));
					}
					
//					for (ProcessWindow processWindow : list) {
//						System.out.println(processWindow);
//					}
					
					processManagementForm.setListProcess(list);
			
					break;
				}
				default:
//					throw new IllegalArgumentException("Unexpected value: " + type);
					System.out.println("Not data");
					break;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public RemoteForm getRemoteForm() {
		return remoteForm;
	}

	public void setRemoteForm(RemoteForm remoteForm) {
		this.remoteForm = remoteForm;
	}

}
