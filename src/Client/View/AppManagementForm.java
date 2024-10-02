package Client.View;

import java.awt.Color;
import java.awt.EventQueue;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import General.AppRunning;
import General.Commands;
import General.ProcessWindow;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class AppManagementForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private Socket socket = null;
	private DataOutputStream dataOutputStream = null;
	private JPanel contentPane;
	private JTable tableListAppRunning;
	private JTextField textFieldName;
	private JTextField textFieldId;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					AppRunningForm frame = new AppRunningForm();
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
	public AppManagementForm(Socket socket) throws IOException {
		this.setSocket(socket);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 407, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		tableListAppRunning = new JTable();
		tableListAppRunning.setBackground(new Color(248, 248, 248));
		tableListAppRunning.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (!e.getValueIsAdjusting()) {
					// Lấy chỉ số của hàng được chọn
					int selectedRow = tableListAppRunning.getSelectedRow();
					if (selectedRow != -1) { // Kiểm tra nếu hàng hợp lệ
						// Lấy dữ liệu từ hàng được chọn
						String Name = tableListAppRunning.getValueAt(selectedRow, 0).toString();
						String Id = tableListAppRunning.getValueAt(selectedRow, 1).toString();
						textFieldName.setText(Name);
						textFieldName.setForeground(Color.BLACK);
						textFieldId.setText(Id);
						textFieldId.setForeground(Color.BLACK);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(tableListAppRunning);
		scrollPane.setBounds(10, 11, 372, 148);
		contentPane.add(scrollPane);

		JButton btnGetListAppRunning = new JButton("Get App Running");
		btnGetListAppRunning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataOutputStream.writeInt(Commands.REQUEST_APP_RUNNING.getAbbrev());
					dataOutputStream.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnGetListAppRunning.setBounds(10, 170, 133, 23);
		contentPane.add(btnGetListAppRunning);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnStart.setBounds(10, 198, 133, 23);
		contentPane.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnStop.setBounds(10, 227, 133, 23);
		contentPane.add(btnStop);

		textFieldName = new JTextField("Enter name app ...");
		textFieldName.setForeground(Color.GRAY);
		textFieldName.setBounds(153, 199, 125, 20);
		textFieldName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(textFieldName.getText().equals("Enter name app ...")) {
					textFieldName.setText("");
					textFieldName.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(textFieldName.getText().isEmpty()) {
					textFieldName.setText("Enter name app ...");
					textFieldName.setForeground(Color.GRAY);
				}
			}
			
		});
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);

		textFieldId = new JTextField("Enter Id ...");
		textFieldId.setForeground(Color.GRAY);
		textFieldId.setBounds(153, 228, 126, 20);
		textFieldId.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(textFieldId.getText().equals("Enter Id ...")) {
					textFieldId.setText("");
					textFieldId.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub 
				if(textFieldId.getText().isEmpty()) {
					textFieldId.setText("Enter Id ...");
					textFieldId.setForeground(Color.GRAY);
				}
			}
			
		});
		contentPane.add(textFieldId);
		textFieldId.setColumns(10);
	}

	public void setListAppRunning(LinkedList<AppRunning> list) {

		// Định nghĩa tên cột cho bảng
		String[] columnNames = { "Name", "Id" };

		// Chuyển đổi LinkedList thành mảng 2 chiều
		Object[][] data = new Object[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			AppRunning process = list.get(i);
			data[i][0] = process.getName();
			data[i][1] = process.getId();
		}

		// Tạo mô hình dữ liệu mới cho JTable
		DefaultTableModel model = new DefaultTableModel(data, columnNames);

		// Gán mô hình dữ liệu cho JTable
		tableListAppRunning.setModel(model);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
