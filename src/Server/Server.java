package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import General.Commands;
import General.Port;

public class Server {
	private ServerSocket serverSocket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private volatile String password;
	private volatile boolean checkConnect = false;

	public void GUI() {

	}

	public void InitServer() {
		try {
			serverSocket = new ServerSocket(Port.port);
			System.out.println("Server is running...");
			
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true) {
						try {
							Socket socketClient = serverSocket.accept();
							System.out.println("Client connected");
							
							new Thread(new ClientHandler(socketClient, password)).start();
						} catch (IOException e) {
							// TODO: handle exception
						}
					}
				}
			}).start();;
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server(String password) {
		this.password = password;
		GUI();
		InitServer();
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
