package Client.View;

import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

import General.Commands;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.Choice;
import java.awt.Dimension;

import javax.swing.JLabel;

public class RemoteForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket = null;
	private ClientForm client = null;
	private DataOutputStream dataOutputStream = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
	private AppManagementForm appManagementForm = null;
	private Choice choiceScale;

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
		
		Dimension sizeClient = Toolkit.getDefaultToolkit().getScreenSize();
		int min = 20;
		int max = 100;
		int widthServer = (int)client.getWidthScreenServer();
		int heigthServer = (int)client.getHeightScreenServer();
		int widthClient = (int)sizeClient.getWidth();
		int heigthClient = (int)sizeClient.getHeight();
		
		System.out.println(widthServer + ", " + heigthServer + ", " + widthClient + ", " + heigthClient);
		
		while((float)widthClient * (float)max / 100 >= widthServer || (float)heigthClient * (float)max / 100 >= heigthServer) {
			--max;
		}
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, (min + max) * 2 / 3);
        
        // Thiết lập các thuộc tính của JSlider
        slider.setMinorTickSpacing(5);  // Dấu nhỏ
        slider.setMajorTickSpacing(20); // Dấu lớn
        slider.setPaintTicks(true);     // Hiển thị dấu
        slider.setPaintLabels(true);    // Hiển thị nhãn giá trị
        slider.setBounds(212, 158, 114, 50);
        contentPane.add(slider);
		
//		choiceScale = new Choice();
//		choiceScale.setBounds(212, 161, 114, 20);
//		choiceScale.add("1");
//		choiceScale.add("0.85");
//		choiceScale.add("0.75");
//		choiceScale.add("0.65");
//		contentPane.add(choiceScale);
		
		JLabel lblNewLabel = new JLabel("Máy chủ");
		lblNewLabel.setBounds(212, 61, 70, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Máy khách");
		lblNewLabel_1.setBounds(212, 111, 70, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel JLabelSizeServer = new JLabel("New label");
		JLabelSizeServer.setBounds(212, 86, 114, 14);
		JLabelSizeServer.setText(Integer.toString(client.getWidthScreenServer()) + " x " + Integer.toString(client.getHeightScreenServer()));
		contentPane.add(JLabelSizeServer);
		
		JLabel JLabelSizeClient = new JLabel("New label");
		JLabelSizeClient.setBounds(212, 133, 114, 14);
		JLabelSizeClient.setText(widthClient + " x " + heigthClient);
		contentPane.add(JLabelSizeClient);
		
		JButton btnResize = new JButton("New button");
		btnResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float scale = (float)slider.getValue() / (float)100;
				client.setScale(scale);
				client.resizeDisplayScreenServer();
			}
		});
		btnResize.setBounds(212, 208, 114, 23);
		contentPane.add(btnResize);
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
