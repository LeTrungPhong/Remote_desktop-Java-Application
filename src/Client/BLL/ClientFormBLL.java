package Client.BLL;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Client.View.AppManagementForm;
import Client.View.KeyloggerForm;
import Client.View.ProcessManagementForm;
import Client.View.RemoteForm;
import General.Commands;

public class ClientFormBLL {
	
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private volatile int widthScreenServer = -1;
	private volatile int heightScreenServer = -1;
	private volatile static float scale = 1;
	private RemoteForm remoteForm = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
	private AppManagementForm appManagementForm = null;
	
	public void connectServer(String address, int port, String password) throws UnknownHostException, IOException {
		socket = new Socket(address, port);
		dataInputStream = new DataInputStream(socket.getInputStream());
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
		setProcessManagementForm(new ProcessManagementForm(socket));
		setKeyloggerForm(new KeyloggerForm(socket));
		setAppManagementForm(new AppManagementForm(socket));
		System.out.println("Connected to server");
	}
	
	public void authenticate(String password) throws IOException {
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
	}
	
	public void resizeScreen() throws HeadlessException, IOException {
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
		}
	}
	
	public Dimension getDimensionSize() {
		return new Dimension(widthScreenServer,heightScreenServer);
	}
	
	public float getScale() {
		return scale;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public ClientFormBLL(String address, int port, String password) {
		try {
			connectServer(address, port, password);
			
			authenticate(password);

			resizeScreen();

		} catch (UnknownHostException u) {
			System.out.println("Lỗi U: " + u);
			return;
		} catch (IOException i) {
			System.out.println("Lỗi I " + i);
			return;
		}
	}

	public RemoteForm getRemoteForm() {
		return remoteForm;
	}

	public void setRemoteForm(RemoteForm remoteForm) {
		this.remoteForm = remoteForm;
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

	public AppManagementForm getAppManagementForm() {
		return appManagementForm;
	}

	public void setAppManagementForm(AppManagementForm appManagementForm) {
		this.appManagementForm = appManagementForm;
	}
}
