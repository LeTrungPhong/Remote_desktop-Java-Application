package Application;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import General.Port;
import Server.Server;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Random;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import Client.ClientForm;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMyID;
	private JTextField textFieldMyPassWord;
	private JTextField textFieldPartnerID;
	private JPasswordField passwordFieldPartnerPassWord;
	private volatile String MACaddress;
	private volatile String passWord;
	private volatile String IPaddress;
	private Server server = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainForm();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
 
	/**
	 * Create the frame.
	 */
	public MainForm() {
		setTitle("Desktop Remote");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 397);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(22, 11, 225, 147);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Cho phep dieu khien");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setForeground(new Color(1, 185, 254));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 205, 27);
//        Border padding = new EmptyBorder(10, 10, 0, 0); 
		Border lineBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
		lblNewLabel.setBorder(lineBorder);
		panel.add(lblNewLabel);

		JTextArea txtrHayGuiId = new JTextArea();
		txtrHayGuiId.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		txtrHayGuiId.setBackground(new Color(240, 240, 240));
		txtrHayGuiId.setEditable(false);
		txtrHayGuiId.setLineWrap(true);
		txtrHayGuiId.setWrapStyleWord(true);
		txtrHayGuiId.setFocusable(false);
		txtrHayGuiId.setHighlighter(null); 
		txtrHayGuiId.setText(
				"Hay gui ID va Mat Khau duoi day cho doi tac neu ban muon cho ho dieu khien may tinh cua minh");
		txtrHayGuiId.setBounds(10, 49, 205, 28);
		panel.add(txtrHayGuiId);

		JLabel lblNewLabel_1 = new JLabel("IP cua ban");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_1.setBounds(10, 91, 63, 14);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Mat khau");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_1_1.setBounds(10, 119, 63, 14);
		panel.add(lblNewLabel_1_1);

		textFieldMyID = new JTextField();
		textFieldMyID.setForeground(new Color(0, 0, 0));
		textFieldMyID.setBackground(new Color(225, 225, 225));
		textFieldMyID.setEditable(false);
		textFieldMyID.setBounds(83, 88, 132, 20);
//		textFieldMyID.setFocusable(false);
		panel.add(textFieldMyID);
		textFieldMyID.setColumns(10);

		textFieldMyPassWord = new JTextField();
		textFieldMyPassWord.setBounds(83, 116, 109, 20);
		panel.add(textFieldMyPassWord);
		textFieldMyPassWord.setBackground(new Color(225, 225, 225));
		textFieldMyPassWord.setEditable(false);
		textFieldMyPassWord.setColumns(10);
		
		setIPaddress();
		setPassWord();

		JButton btnChangePassword = new JButton("");
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPassWord();
				if (server != null) {
		            setPassWordServer();
		        } else {
		            JOptionPane.showMessageDialog(MainForm.this, "Server chưa được khởi tạo!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
		
		btnChangePassword.setBounds(195, 116, 20, 20);
		panel.add(btnChangePassword);
		ImageIcon icon = new ImageIcon("src/image/reloadIcon.png");
		Image scaledImage = icon.getImage().getScaledInstance(btnChangePassword.getWidth(), btnChangePassword.getHeight(),
				Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		btnChangePassword.setIcon(scaledIcon);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(252, 11, 225, 147);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("Dieu khien may tinh khac");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2.setForeground(new Color(1, 185, 254));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(10, 11, 205, 27);
//		MatteBorder bottomBorder = new MatteBorder(0, 0, 1, 0, Color.GRAY);
		lblNewLabel_2.setBorder(lineBorder);
		panel_1.add(lblNewLabel_2);

		JTextArea txtrHayNhapId = new JTextArea();
		txtrHayNhapId.setWrapStyleWord(true);
		txtrHayNhapId.setText("Hay nhap ID va Password cua may ban can dieu khien");
		txtrHayNhapId.setLineWrap(true);
		txtrHayNhapId.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		txtrHayNhapId.setFocusable(false);
		txtrHayNhapId.setEditable(false);
		txtrHayNhapId.setBackground(UIManager.getColor("Button.background"));
		txtrHayNhapId.setBounds(10, 52, 205, 28);
		panel_1.add(txtrHayNhapId);

		JLabel lblNewLabel_1_2 = new JLabel("IP doi tac");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_1_2.setBounds(10, 91, 63, 14);
		panel_1.add(lblNewLabel_1_2);

		textFieldPartnerID = new JTextField();
		textFieldPartnerID.setColumns(10);
		textFieldPartnerID.setBounds(79, 88, 136, 20);
		panel_1.add(textFieldPartnerID);

		JLabel lblNewLabel_1_1_1 = new JLabel("Mat khau");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_1_1_1.setBounds(10, 119, 63, 14);
		panel_1.add(lblNewLabel_1_1_1);

		passwordFieldPartnerPassWord = new JPasswordField();
		passwordFieldPartnerPassWord.setBounds(79, 116, 136, 20);
		panel_1.add(passwordFieldPartnerPassWord);

		JButton btnStartRemote = new JButton("Bat dau dieu khien");
		btnStartRemote.setBackground(new Color(245, 245, 245));
		btnStartRemote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String IPPartner = textFieldPartnerID.getText();
				String passwordPartner = new String(passwordFieldPartnerPassWord.getPassword());
				System.out.println("IP Partner: " + IPPartner);
				System.out.println("Password Partner: " + passwordPartner);
				if (IPPartner.isEmpty() || passwordPartner.isEmpty()) {
					JOptionPane.showMessageDialog(MainForm.this, "Khong duoc bo trong IP hoac password", "Thong bao",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					try {
						JOptionPane.showMessageDialog(
							    MainForm.this, 
							    "Máy tính đang được điều khiển!", 
							    "Thông báo",
							    JOptionPane.INFORMATION_MESSAGE
							);
						new ClientForm(textFieldPartnerID.getText(), Port.port, passwordPartner);
						setVisible(false);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnStartRemote.setBounds(293, 169, 156, 23);
		contentPane.add(btnStartRemote);

		setVisible(true);
		
//		new Thread(new runThreadServer(this.server,this.passWord)).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainForm.this.server = new Server(passWord);
				System.out.println("Khoi tao server thanh cong ...");
			}
		}).start();
	}

	public void setMACaddress() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
			byte[] mac = networkInterface.getHardwareAddress();
			System.out.print("MAC address  : ");
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			MACaddress = stringBuilder.toString();
			System.out.println(stringBuilder.toString());
			textFieldMyID.setText(stringBuilder.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setIPaddress() {
		try {
			InetAddress IP = InetAddress.getLocalHost();
			System.out.println("IP of my system is := " + IP.getHostAddress());
			IPaddress = IP.getHostAddress();
//		     Port.ipAddress = IP.getHostAddress();
			textFieldMyID.setText(IP.getHostAddress());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setPassWord() {
		Random random = new Random();
		int min = 10000;
		int max = 99999;
		int randomNumber = random.nextInt(max - min + 1) + min;
		System.out.println("Random number between " + min + " and " + max + ": " + randomNumber);
		this.passWord = Integer.toString(randomNumber);
		textFieldMyPassWord.setText(Integer.toString(randomNumber));
	}
	
	public void setPassWordServer() {
		this.server.setPassword(this.passWord);
	}
}
