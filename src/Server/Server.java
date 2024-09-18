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
//	private JLabel jLabelScreen;
//	private JButton jbutton;
	private ServerSocket serverSocket = null;
	private Socket socketClient = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private volatile String password;
	private volatile boolean checkConnect = false;

	public void GUI() {
//		setTitle("Server");
//		int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
//		int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
//
//		setBounds(width / 2, height / 2, width, height);
//		setLayout(new GridBagLayout());
//		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//		jLabelScreen = new JLabel();
//		jbutton = new JButton("Screen");
//		add(jLabelScreen);
//
//		setVisible(true);
	}

	public void InitServer() {
		try {
			serverSocket = new ServerSocket(Port.port);
			System.out.println("Server is running...");
			
			while(true) {
				socketClient = serverSocket.accept();
				System.out.println("Client connected");
				
				dataInputStream = new DataInputStream(socketClient.getInputStream());
				dataOutputStream = new DataOutputStream(socketClient.getOutputStream());
				
				while(true) {
					if(dataInputStream.readInt() == Commands.REQUEST_CONNECT.getAbbrev()) {
						String passwordClient = dataInputStream.readUTF();
						System.out.println(passwordClient + " " + this.password);
						if(passwordClient.equals(this.password)) {
							System.out.println("Mat khau hop le");
							dataOutputStream.writeInt(Commands.RESPONSE_CONNECT.getAbbrev());
							dataOutputStream.writeBoolean(true);
							checkConnect = true;
						} else {
							System.out.println("Mat khau khong hop le");
							dataOutputStream.writeInt(Commands.RESPONSE_CONNECT.getAbbrev());
							dataOutputStream.writeBoolean(false);
						}
						break;
					}
				}
				if(checkConnect) {
					break;
				}
			}
			
			new Thread(new SendScreen(socketClient)).start();

//			new Thread(new ReceiveEvents(socketClient)).start();
			
			new Thread(new CentralReader(socketClient)).start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server(String password) {
		this.password = password;
		GUI();
		InitServer();
	}

//	public static void main(String[] args) {
//		new Server();
//	}
}
