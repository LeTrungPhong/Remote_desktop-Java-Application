package Client;

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
import java.awt.event.ActionEvent;

public class ProcessManagementForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableListProcess;
	private JTextField textFieldProcess;
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;

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
		setBounds(100, 100, 355, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tableListProcess = new JTable();
		JScrollPane scrollPane = new JScrollPane(tableListProcess); // Thêm JScrollPane
	    scrollPane.setBounds(0, 0, 339, 155);
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
		btnGetListProcess.setBounds(10, 166, 148, 23);
		contentPane.add(btnGetListProcess);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldProcess.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ProcessManagementForm.this, "Khong duoc bo trong", "Thong bao", JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						dataOutputStream.writeInt(Commands.REQUEST_START_PROCESS.getAbbrev());
						dataOutputStream.writeUTF(textFieldProcess.getText().trim());
						dataOutputStream.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnStart.setBounds(10, 200, 109, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldProcess.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ProcessManagementForm.this, "Khong duoc bo trong", "Thong bao", JOptionPane.WARNING_MESSAGE);
				}
				try {
					dataOutputStream.writeInt(Commands.REQUEST_STOP_PROCESS.getAbbrev());
					dataOutputStream.writeUTF(textFieldProcess.getText());
					
				} catch (IOException err) {
					err.printStackTrace();
				}
			}
		});
		btnStop.setBounds(10, 227, 109, 23);
		contentPane.add(btnStop);
		
		textFieldProcess = new JTextField();
		textFieldProcess.setBounds(141, 213, 86, 20);
		contentPane.add(textFieldProcess);
		textFieldProcess.setColumns(10);
		
		
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
