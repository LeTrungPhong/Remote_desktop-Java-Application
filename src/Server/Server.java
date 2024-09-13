package Server;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import General.Port;

public class Server extends JFrame implements ActionListener {
	private JLabel jLabelScreen;
	private JButton jbutton;
	private ServerSocket serverSocket = null;
	private Socket socketClient = null;

	public void GUI() {
		setTitle("Server");
		int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

		setBounds(width / 2, height / 2, width, height);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabelScreen = new JLabel();
		jbutton = new JButton("Screen");
		add(jLabelScreen);

		setVisible(true);
	}

	public void InitServer() {
		try {
			serverSocket = new ServerSocket(Port.port);
			System.out.println("Server is running...");

			socketClient = serverSocket.accept();
			System.out.println("Client connected");
			
			new Thread(new SendScreen(socketClient)).start();

			new Thread(new ReceiveEvents(socketClient)).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server() {
		GUI();
		InitServer();
	}

	public static void main(String[] args) {
		new Server();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jbutton) {

		}
	}
}
