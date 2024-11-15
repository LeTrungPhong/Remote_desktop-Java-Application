package Client;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Application.MainForm;
import General.Commands;
import General.ProcessWindow;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.MatteBorder;

public class ProcessManagementForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableListProcess;
	private JTextField textFieldProcessImageName;
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private JTextField textFieldProcessPID; 

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ProcessManagementForm frame = new ProcessManagementForm();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public ProcessManagementForm(Socket socket) throws IOException {
		this.setSocket(socket);
		dataInputStream = new DataInputStream(socket.getInputStream());
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 418, 371);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tableListProcess = new JTable();
		tableListProcess.setBackground(new Color(248,248,248));
		
		tableListProcess.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
                if (!e.getValueIsAdjusting()) {
                    // Lấy chỉ số của hàng được chọn
                    int selectedRow = tableListProcess.getSelectedRow();
                    if (selectedRow != -1) { // Kiểm tra nếu hàng hợp lệ
                        // Lấy dữ liệu từ hàng được chọn
                        String ImageName = tableListProcess.getValueAt(selectedRow, 0).toString();
                        String PID = tableListProcess.getValueAt(selectedRow, 1).toString();
                        textFieldProcessImageName.setText(ImageName);
                        textFieldProcessImageName.setForeground(Color.BLACK);
                        textFieldProcessPID.setText(PID);
                        textFieldProcessPID.setForeground(Color.BLACK);
                    }
                }
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(tableListProcess);
	    scrollPane.setBounds(10, 11, 382, 206);
	    contentPane.add(scrollPane); // Thêm JScrollPane vào panel
//		tableListProcess.setBounds(0, 0, 339, 155);
//		contentPane.add(tableListProcess);
		
		JButton btnGetListProcess = new JButton("Get List Process");
		btnGetListProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataOutputStream.writeInt(Commands.REQUEST_PROCESS.getAbbrev());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnGetListProcess.setBounds(10, 230, 148, 23);
		contentPane.add(btnGetListProcess);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldProcessImageName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ProcessManagementForm.this, "Khong duoc bo trong", "Thong bao", JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						dataOutputStream.writeInt(Commands.REQUEST_START_PROCESS.getAbbrev());
						dataOutputStream.writeUTF(textFieldProcessImageName.getText().trim());
						dataOutputStream.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnStart.setBounds(10, 264, 109, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldProcessPID.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ProcessManagementForm.this, "Khong duoc bo trong", "Thong bao", JOptionPane.WARNING_MESSAGE);
				}
				try {
					dataOutputStream.writeInt(Commands.REQUEST_STOP_PROCESS.getAbbrev());
					dataOutputStream.writeUTF(textFieldProcessPID.getText());
					dataOutputStream.flush();
				} catch (IOException err) {
					err.printStackTrace();
				}
			}
		});
		btnStop.setBounds(10, 298, 109, 23);
		contentPane.add(btnStop);
		
		textFieldProcessImageName = new JTextField("Enter ImageName ...");
		textFieldProcessImageName.setForeground(Color.GRAY);
		textFieldProcessImageName.setBounds(129, 264, 136, 20);
		textFieldProcessImageName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(textFieldProcessImageName.getText().equals("Enter ImageName ...")) {
					textFieldProcessImageName.setText("");
					textFieldProcessImageName.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(textFieldProcessImageName.getText().isEmpty()) {
					textFieldProcessImageName.setText("Enter ImageName ...");
					textFieldProcessImageName.setForeground(Color.GRAY);
				}
			}
			
		});
		contentPane.add(textFieldProcessImageName);
		textFieldProcessImageName.setColumns(10);
		
		textFieldProcessPID = new JTextField("Enter PID ...");
		textFieldProcessPID.setForeground(Color.GRAY);
		textFieldProcessPID.setBounds(129, 299, 136, 20);
		
		textFieldProcessPID.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(textFieldProcessPID.getText().isEmpty()) {
					textFieldProcessPID.setText("Enter PID ...");
					textFieldProcessPID.setForeground(Color.GRAY);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(textFieldProcessPID.getText().equals("Enter PID ...")) {
					textFieldProcessPID.setText("");
					textFieldProcessPID.setForeground(Color.BLACK);
				}
			}
		});
		
		contentPane.add(textFieldProcessPID);
		textFieldProcessPID.setColumns(10);
		
		
	}
	
	public void setListProcess(LinkedList<ProcessWindow> list) {
		
		 // Định nghĩa tên cột cho bảng
	    String[] columnNames = { "ImageName", "PID", "SessionName", "SessionIDs", "MemUsage" };
	    
	    // Chuyển đổi LinkedList thành mảng 2 chiều
	    Object[][] data = new Object[list.size()][5];
	    for (int i = 0; i < list.size(); i++) {
	        ProcessWindow process = list.get(i);
	        data[i][0] = process.getImageName();
	        data[i][1] = process.getPID();
	        data[i][2] = process.getSessionName();
	        data[i][3] = process.getSessionIDs();
	        data[i][4] = process.getMemUsage();
	    }

	    // Tạo mô hình dữ liệu mới cho JTable
	    DefaultTableModel model = new DefaultTableModel(data, columnNames);
	    
	    // Gán mô hình dữ liệu cho JTable
	    tableListProcess.setModel(model);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
