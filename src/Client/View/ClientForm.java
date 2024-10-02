package Client.View;

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
import Client.BLL.CentralReader;
import Client.BLL.ClientFormBLL;
import Client.BLL.SendEvents;
import General.Commands;
import General.Port;

public class ClientForm extends JFrame {
	private Socket socket = null;
	private JLabel jLabelScreen;
	private InputStream inputStream = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private volatile int widthScreenServer = -1;
	private volatile int heightScreenServer = -1;
	private volatile float scale = 1;
	private RemoteForm remoteForm = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
	private AppManagementForm appManagementForm = null;
	private ClientFormBLL clientFormBLL = null;

	public void GUI() {
		setTitle("Client");
		setResizable(false);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

		setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabelScreen = new JLabel();

		add(jLabelScreen);

		setVisible(true);
	}

	public ClientForm(String address, int port, String password) throws IOException {

		GUI();
		
		clientFormBLL = new ClientFormBLL(address, port, password);
		
		this.socket = clientFormBLL.getSocket();
		this.dataInputStream = new DataInputStream(socket.getInputStream());
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		
		Dimension sizeScreen = clientFormBLL.getDimensionSize();
		
		this.widthScreenServer = (int)sizeScreen.getWidth();
		this.heightScreenServer = (int)sizeScreen.getHeight();
		this.scale = clientFormBLL.getScale();
 
		Insets insets = this.getInsets();

		setBounds(0, 0, widthScreenServer + insets.left + insets.right,
				heightScreenServer + insets.bottom + insets.top);
		
		new SendEvents(this.socket, this, scale);
		
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
}
