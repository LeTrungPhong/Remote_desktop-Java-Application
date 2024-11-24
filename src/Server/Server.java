package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Application.MainForm;
import General.Commands;
import General.Port;

public class Server {
	private ServerSocket serverSocket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private volatile String password;
	private volatile boolean checkConnect = false;
	private MainForm mainForm = null;

	public void InitServer() {
		try {
			System.out.println(Port.port);
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
							
							new Thread(new ClientHandler(socketClient, password, Server.this.mainForm)).start();
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

	public Server(String password, MainForm mainForm) {
		this.mainForm = mainForm;
		this.password = password;
		this.mainForm = mainForm;
		InitServer();
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
