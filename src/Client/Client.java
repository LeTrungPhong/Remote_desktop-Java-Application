package Client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import Application.MainForm;
import General.Commands;
import General.Port;

public class Client extends JFrame {
	private Socket socket = null;
	private JLabel jLabelScreen;
	private JButton jButtonShutdown;
	private JButton jButtonBlock;
	private JButton jButtonGetProcess;
	private InputStream inputStream = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private volatile int widthScreenServer = -1;
	private volatile int heightScreenServer = -1;
	private volatile static float scale = 1;
	private RemoteForm remoteForm = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
	private AppManagementForm appManagementForm = null;

	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());

		Graphics2D g2d = resizedImage.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		g2d.dispose();

		return resizedImage;
	}

	public void GUI() {
		setTitle("Client");
		setResizable(false);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

//		setBounds(width / 2, height / 2, width, height);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabelScreen = new JLabel();
		jButtonShutdown = new JButton("Shut down");
		jButtonBlock = new JButton("Block");
		jButtonGetProcess = new JButton("GET PROCESS");

//		jButtonGetProcess.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					dataOutputStream.writeInt(Commands.REQUEST_PROCESS.getAbbrev());
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		});

		add(jLabelScreen);
//		add(jButtonGetProcess);
//		add(jButtonBlock);
//		add(jButtonShutdown);

//		setUndecorated(true);

		setVisible(true);

		
	}

	// constructor to put ip address and port
	public Client(String address, int port, String password) {

		GUI();
 
		// establish a connection
		try {
			socket = new Socket(address, port);
			processManagementForm = new ProcessManagementForm(socket);
			keyloggerForm = new KeyloggerForm(socket);
			appManagementForm = new AppManagementForm(socket);
			System.out.println("Connected to server");

			// sends output to the socket

			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());

			dataOutputStream.writeInt(Commands.REQUEST_CONNECT.getAbbrev());
			dataOutputStream.writeUTF(password);

			while (true) {
				if (dataInputStream.readInt() == Commands.RESPONSE_CONNECT.getAbbrev()) {
					if (dataInputStream.readBoolean()) {
						System.out.println("Mat khau hop le");
						break;
					} else {
						System.out.println("Mat khau khong hop le");
						socket.close();
						return;
					}
				}
			}

			if (dataInputStream.readInt() == Commands.SIZE_SERVER.getAbbrev()) {
				// dat lai kich thuoc
				widthScreenServer = dataInputStream.readInt();
				heightScreenServer = dataInputStream.readInt();

				try {
					Robot robot = new Robot();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

					double widthClient = screenSize.getWidth();
					double heigthClient = screenSize.getHeight();

					while (true) {
						if (widthClient > widthScreenServer && heigthClient > heightScreenServer)
							break;
						widthScreenServer = widthScreenServer * 2 / 5;
						heightScreenServer = heightScreenServer * 2 / 5;
						scale = scale * 2 / 5;
					}

				} catch (AWTException err) {
					err.printStackTrace();
				}

				Insets insets = this.getInsets();

				setBounds(0, 0, widthScreenServer + insets.left + insets.right,
						heightScreenServer + insets.bottom + insets.top);
			}

			new SendEvents(this.socket, this, scale);

			new Thread(new CentralReader(this.socket, this)).start();
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						remoteForm = new RemoteForm(socket,Client.this); 
						remoteForm.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (UnknownHostException u) {
			System.out.println("Lỗi U: " + u);
			return;
		} catch (IOException i) {
			System.out.println("Lỗi I " + i);
			return;
		}
		
		
	}
	
	public JLabel getLabelScreen() {
		return this.jLabelScreen;
	}

	public int getWidthScreenServer() {
		return widthScreenServer;
	}

	public int getHeightScreenServer() {
		return heightScreenServer;
	}

	public float getScale() {
		return scale;
	}
	
	public RemoteForm getRemoteForm() {
		return remoteForm;
	}
	
	public ProcessManagementForm getProcessManagementForm() {
		return processManagementForm;
	}
	
	public KeyloggerForm getKeyloggerForm() {
		return keyloggerForm;
	}

	public AppManagementForm getAppManagementForm() {
		return appManagementForm;
	}

	public void setAppManagementForm(AppManagementForm appManagementForm) {
		this.appManagementForm = appManagementForm;
	}

//	public static void main(String args[]) {
//		new Client(Port.ipAddress, Port.port, );
//	}

}
