package Client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import General.Commands;
import General.Port;
import Server.ReceiveEvents;
import Server.SendScreen;

public class Client extends JFrame {
	private Socket socket = null;
	private JLabel jLabelScreen;
	private JButton jButtonShutdown;
	private JButton jButtonBlock;
	private InputStream inputStream = null;
	private DataInputStream dataInputStream = null;
	private volatile int widthScreenServer = -1;
	private volatile int heightScreenServer = -1;
	private volatile static float scale = 1;
	
	public JLabel getLabelScreen() {
		return this.jLabelScreen;
	}
	
	public int getWidthScreenServer() {
		return widthScreenServer;
	}
	
	public int getHeightScreenServer() {
		return heightScreenServer;
	}
	
	public float getScale() {
		return scale;
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());

		Graphics2D g2d = resizedImage.createGraphics();	

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		g2d.dispose();

		return resizedImage;
	}

	public void GUI() {
		setTitle("Client");
		int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

//		setBounds(width / 2, height / 2, width, height);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabelScreen = new JLabel();
		jButtonShutdown = new JButton("Shut down");
		jButtonBlock = new JButton("Block");
		
		add(jLabelScreen);
//		add(jButtonBlock);
//		add(jButtonShutdown);
		
//		setUndecorated(true);

		setVisible(true);
	}

	// constructor to put ip address and port
	public Client(String address, int port) {

		GUI();

		// establish a connection
		try {
			socket = new Socket(address, port);
			System.out.println("Connected to server");

			// sends output to the socket

			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);

			if(dataInputStream.readInt() == Commands.SIZE_SERVER.getAbbrev()) {
				// dat lai kich thuoc
				widthScreenServer = dataInputStream.readInt();
				heightScreenServer = dataInputStream.readInt();

				try {
					Robot robot = new Robot();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

					double widthClient = screenSize.getWidth();
					double heigthClient = screenSize.getHeight();

					while (true) {
						if (widthClient > widthScreenServer && heigthClient > heightScreenServer)
							break;
						widthScreenServer = widthScreenServer * 4 / 5;
						heightScreenServer = heightScreenServer * 4 / 5;
						scale = scale * 4 / 5;
					}

				} catch (AWTException err) {
					err.printStackTrace();
				}
				
				Insets insets = this.getInsets();

				setBounds(0, 0, widthScreenServer + insets.left + insets.right, heightScreenServer + insets.bottom + insets.top);
			}
			
			new SendEvents(this.socket,this,scale);
			
			new Thread(new ReceiveScreen(this.socket,this)).start();

		} catch (UnknownHostException u) {
			System.out.println(u);
			return;
		} catch (IOException i) {
			System.out.println(i);
			return;
		}
	}

	public static void main(String args[]) {
		new Client(Port.ipAddress, Port.port);
	}
	
}
