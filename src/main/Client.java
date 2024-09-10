package main;

import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class Client extends JFrame implements MouseListener, ActionListener, MouseMotionListener{
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream out = null;
	private JLabel jLabelScreen;
	private InputStream inputStream = null;
	private DataInputStream dataInputStream = null;
	private static volatile int mouseX = -1;
	private static volatile int mouseY = -1;
	
	public void GUI() {
		setTitle("Client");
		int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
		
		setBounds(width / 2, height / 2, width, height);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		jLabelScreen = new JLabel();
		add(jLabelScreen);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setVisible(true);
	}

	// constructor to put ip address and port
	public Client(String address, int port) {

		GUI();
		
		// establish a connection
		try {
			socket = new Socket(address, port);
			System.out.println("Connected");

			// takes input from terminal
			input = new DataInputStream(System.in);

			// sends output to the socket
			out = new DataOutputStream(socket.getOutputStream());
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			
			Thread sendActionToServer = new Thread(() -> {
				try {
					while(true) {
						out.writeInt(mouseX);
						out.writeInt(mouseY);
						System.out.println("Mouse to X: " + mouseX + ", Y: " + mouseY);
						Thread.sleep(100);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			});
			
			sendActionToServer.start();
			
			while(true) {
				// Nhận dữ liệu từ server
	            
	            // Đọc kích thước của ảnh trước
	            byte[] sizeAr = new byte[4];
	            
	            inputStream.read(sizeAr);
	            // datadataInputStream
	            
	            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
	            
	            // Nhận dữ liệu ảnh
	            byte[] imageBytes = new byte[size];
	            inputStream.read(imageBytes);
	            // // datadataInputStream
	            
	            // Chuyển đổi lại thành BufferedImage
	            BufferedImage receivedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
	            
	            jLabelScreen.setIcon(new ImageIcon(receivedImage));
	            repaint();
			}
			
		} catch (UnknownHostException u) {
			System.out.println(u);
			return;
		} catch (IOException i) {
			System.out.println(i);
			return;
		} finally {
			try {
				input.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public static void main(String args[]) {
		new Client(Port.ipAddress, Port.port);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
			
			if(e.getButton() == MouseEvent.BUTTON1) {
				System.out.println("You left click the mouse at " + point.x + " " + point.y);
			} else if(e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
				System.out.println("You right click the mouse at " + point.x + " " + point.y);
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
			System.out.println("You press the mouse at " + point.x + " " + point.y);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
			System.out.println("You release the mouse at " + point.x + " " + point.y);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
			System.out.println("You enter the window at " + point.x + " " + point.y);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
			System.out.println("You exit the window at " + point.x + " " + point.y);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
//        System.out.println("Mouse dragged to X: " + mouseX + ", Y: " + mouseY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
//        System.out.println("Mouse moved to X: " + mouseX + ", Y: " + mouseY);
	}
}
