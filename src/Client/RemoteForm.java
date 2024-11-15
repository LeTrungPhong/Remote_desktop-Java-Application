package Client;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Choice;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.awt.Color;

public class RemoteForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket = null;
	private ClientForm client = null;
	private DataOutputStream dataOutputStream = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
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
		this.setTitle("Remote Form");
		this.setSocket(socket);
		this.setClient(client);
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		processManagementForm = client.getProcessManagementForm();
		keyloggerForm = client.getKeyloggerForm();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 390, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnProcessManagement = new JButton("Quản lý tiến trình");
		btnProcessManagement.setHorizontalAlignment(SwingConstants.LEFT);
		btnProcessManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processManagementForm.setVisible(true);
			}
		});
		btnProcessManagement.setBounds(10, 117, 176, 23);
		contentPane.add(btnProcessManagement);

		JButton btnScreenShot = new JButton("Chụp ảnh màn hình");
		btnScreenShot.setHorizontalAlignment(SwingConstants.LEFT);
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
		btnScreenShot.setBounds(10, 140, 176, 23);
		contentPane.add(btnScreenShot);

		JButton btnKeylogger = new JButton("Lắng nghe sự kiện bàn phím");
		btnKeylogger.setHorizontalAlignment(SwingConstants.LEFT);
		btnKeylogger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyloggerForm.setVisible(true);
			}
		});
		btnKeylogger.setBounds(10, 172, 176, 25);
		contentPane.add(btnKeylogger);

		JButton btnShutdown = new JButton("Tắt nguồn");
		btnShutdown.setHorizontalAlignment(SwingConstants.LEFT);
		btnShutdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataOutputStream.writeInt(Commands.REQUEST_SHUTDOWN.getAbbrev());
					dataOutputStream.flush();
				} catch(Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnShutdown.setBounds(10, 204, 176, 23);
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
        slider.setBounds(206, 158, 146, 50);
        contentPane.add(slider);
		
		JButton btnResize = new JButton("Xác nhận");
		btnResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float scale = (float)slider.getValue() / (float)100;
				client.setScale(scale);
				client.resizeDisplayScreenServer();
			}
		});
		btnResize.setBounds(206, 219, 146, 23);
		contentPane.add(btnResize);
		
		JLabel text = new JLabel("Bảng điều khiển");
		text.setFont(new Font("Times New Roman", Font.BOLD, 16));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setBounds(118, 11, 146, 25);
		contentPane.add(text);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(196, 117, 168, 133);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Điều chỉnh tỉ lệ màn hình");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(new Color(62, 62, 62));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_2.setBounds(10, 21, 148, 14);
		panel.add(lblNewLabel_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 45, 354, 61);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel JLabelSizeServer = new JLabel("New label");
		JLabelSizeServer.setBounds(138, 11, 114, 14);
		panel_1.add(JLabelSizeServer);
		JLabelSizeServer.setText(Integer.toString(client.getWidthScreenServer()) + " x " + Integer.toString(client.getHeightScreenServer()));
		
//		choiceScale = new Choice();
//		choiceScale.setBounds(212, 161, 114, 20);
//		choiceScale.add("1");
//		choiceScale.add("0.85");
//		choiceScale.add("0.75");
//		choiceScale.add("0.65");
//		contentPane.add(choiceScale);
		
		JLabel lblNewLabel = new JLabel("Máy chủ");
		lblNewLabel.setBounds(10, 11, 70, 14);
		panel_1.add(lblNewLabel);
		
		JLabel JLabelSizeClient = new JLabel("New label");
		JLabelSizeClient.setBounds(138, 36, 114, 14);
		panel_1.add(JLabelSizeClient);
		JLabelSizeClient.setText(widthClient + " x " + heigthClient);
		
		JLabel lblNewLabel_1 = new JLabel("Máy khách");
		lblNewLabel_1.setBounds(10, 36, 70, 14);
		panel_1.add(lblNewLabel_1);
		
		JButton btnDisconnect = new JButton("Ngắt kết nối");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataOutputStream.writeInt(Commands.REQUEST_DISCONNECT.getAbbrev());
					dataOutputStream.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDisconnect.setHorizontalAlignment(SwingConstants.LEFT);
		btnDisconnect.setBounds(10, 227, 176, 23);
		contentPane.add(btnDisconnect);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                    RemoteForm.this,
                    "Bạn có chắc muốn ngắt kết nối?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                	try {
                        if (dataOutputStream != null) {
                            dataOutputStream.writeInt(Commands.REQUEST_DISCONNECT.getAbbrev());
                            dataOutputStream.flush();
                            dataOutputStream.close();
                        }
                        client.stopThreadCentralReader();
                        if (socket != null && !socket.isClosed()) {
                            socket.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        RemoteForm.this.dispose();
                        client.dispose();
                    }
                }
            }
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
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
}
