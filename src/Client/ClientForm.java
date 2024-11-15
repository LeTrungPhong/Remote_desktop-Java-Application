package Client;

import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import General.Commands;


public class ClientForm extends JFrame {
	private Socket socket = null;
	private JLabel jLabelScreen;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private volatile int widthScreenServer = -1;
	private volatile int heightScreenServer = -1;
	private volatile float scale = 1;
	private RemoteForm remoteForm = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
	private SendEvents sendEvents = null;

	public void GUI() {
		setTitle("Client");
		setResizable(false);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabelScreen = new JLabel();

		add(jLabelScreen);

		setVisible(true);
	}

	public ClientForm(String address, int port, String password) throws IOException {

		GUI();
		
		
		
		///////////////////////////////////////////////////
		
		this.socket = new Socket(address, port);
		this.dataInputStream = new DataInputStream(socket.getInputStream());
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		this.setProcessManagementForm(new ProcessManagementForm(socket));
		this.setKeyloggerForm(new KeyloggerForm(socket));
		
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
			this.widthScreenServer = dataInputStream.readInt();
			this.heightScreenServer = dataInputStream.readInt();

//			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//
//			double widthClient = screenSize.getWidth();
//			double heigthClient = screenSize.getHeight();
//
//			while (true) {
//				if (widthClient > widthScreenServer && heigthClient > heightScreenServer)
//					break;
//				widthScreenServer = widthScreenServer * 2 / 5;
//				heightScreenServer = heightScreenServer * 2 / 5;
//				scale = scale * 2 / 5;
//			}
		}
		
		
//		clientFormBLL = new ClientFormBLL(address, port, password);
		
//		this.setProcessManagementForm(clientFormBLL.getProcessManagementForm());
//		this.setKeyloggerForm(clientFormBLL.getKeyloggerForm());
//		this.socket = clientFormBLL.getSocket();
//		this.dataInputStream = new DataInputStream(socket.getInputStream());
//		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		
//		Dimension sizeScreen = clientFormBLL.getDimensionSize();
		
//		this.widthScreenServer = (int)sizeScreen.getWidth();
//		this.heightScreenServer = (int)sizeScreen.getHeight();
		
		
		
		
		
		////////////////////////////////////////
		
		resizeDisplayScreenServer();
		
		sendEvents = new SendEvents(this.socket, this, scale);
		
		new Thread(new CentralReader(this.socket, this)).start();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					remoteForm = new RemoteForm(socket,ClientForm.this); 
					remoteForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void resizeDisplayScreenServer() {
		Insets insets = this.getInsets();

		setBounds(0, 0, ((int)(widthScreenServer * this.scale)) + insets.left + insets.right,
				((int)(heightScreenServer * this.scale)) + insets.bottom + insets.top);
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
	
	public void setScale(float scale) {
		this.scale = scale;
		sendEvents.setScale(scale);
	}
	
	public RemoteForm getRemoteForm() {
		return remoteForm;
	}
	
	public ProcessManagementForm getProcessManagementForm() {
		return processManagementForm;
	}
	
	public void setProcessManagementForm(ProcessManagementForm processManagementForm) {
		this.processManagementForm = processManagementForm;
	}
	
	public KeyloggerForm getKeyloggerForm() {
		return keyloggerForm;
	}
	
	public void setKeyloggerForm(KeyloggerForm keyloggerForm) {
		this.keyloggerForm = keyloggerForm;
	}
}
