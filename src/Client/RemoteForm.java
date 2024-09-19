package Client;

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
	private DataOutputStream dataOutputStream = null;
	private ProcessManagementForm processManagementForm = null;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					RemoteForm frame = new RemoteForm();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public RemoteForm(Socket socket, ProcessManagementForm processManagementForm) throws IOException {
		this.setSocket(socket);
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		this.processManagementForm = processManagementForm;

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
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							processManagementForm.setVisible(true);
//							dataOutputStream.writeInt(Commands.OPEN_FORM_PROCESS_MANAGEMENT.getAbbrev());
//							dataOutputStream.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnProcessManagement.setBounds(33, 57, 135, 23);
		contentPane.add(btnProcessManagement);

		JButton btnAppManagement = new JButton("App Management");
		btnAppManagement.setBounds(33, 91, 135, 23);
		contentPane.add(btnAppManagement);

		JButton btnScreenShot = new JButton("Screen Shot");
		btnScreenShot.setBounds(33, 124, 135, 23);
		contentPane.add(btnScreenShot);

		JButton btnKeylogger = new JButton("Keylogger");
		btnKeylogger.setBounds(33, 158, 135, 23);
		contentPane.add(btnKeylogger);

		JButton btnShutdown = new JButton("Shutdown");
		btnShutdown.setBounds(33, 192, 135, 23);
		contentPane.add(btnShutdown);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
