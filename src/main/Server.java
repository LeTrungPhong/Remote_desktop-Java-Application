package main;

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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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

public class Server extends JFrame implements ActionListener {

	private JLabel jLabelScreen;
	private JButton jbutton;
	private ServerSocket serverSocket = null;
	private OutputStream outputStream = null;
	private DataInputStream dataInputStream = null;

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

	public void ThreadSendImage() {
		try {
			serverSocket = new ServerSocket(Port.port);
			System.out.println("Server is running...");

			Socket socketClient = serverSocket.accept();
			System.out.println("Client connected");

			Thread sendImage = new Thread(() -> {
				try {
					Robot robot = new Robot();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					Rectangle screenRect = new Rectangle(screenSize);

					int screenShotCount = 0;

					while (true) {
						// chup man hinh
						BufferedImage screenCapture = robot.createScreenCapture(screenRect);

						// sua lai kich thuoc
						BufferedImage resizedImage = Server.resizeImage(screenCapture, getWidth(), getHeight());

						// luu anh thanh file
//						try {
//							String fileName = "screenshot" + screenShotCount + ".png";
//							ImageIO.write(screenCapture, "png", new File(fileName));
//							System.out.println("Saved screenshot: " + fileName);
//						} catch (IOException e) {
//		                    e.printStackTrace();
//		                }

						// Chuyển đổi ảnh thành byte array
						ByteArrayOutputStream baos = new ByteArrayOutputStream();

						ImageIO.write(resizedImage, "jpg", baos);
						// Phương thức ImageIO.write(image, "png", baos) trong Java được
						// sử dụng để ghi một đối tượng ảnh (BufferedImage) vào một luồng đầu
						// ra dưới định dạng ảnh nhất định (ở đây là PNG) và lưu vào
						// ByteArrayOutputStream
						// (một luồng đầu ra trong bộ nhớ).

						byte[] imageBytes = ByteBuffer.allocate(4).putInt(baos.size()).array();

						outputStream = socketClient.getOutputStream();

						outputStream.write(imageBytes);
						outputStream.write(baos.toByteArray());
						outputStream.flush();

						// cap nhat hinh anh tren JFrame
						jLabelScreen.setIcon(new ImageIcon(resizedImage));
						repaint();

						// dung 1 giay truoc khi chup lai
						Thread.sleep(100);
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			});

			sendImage.start();
			
			dataInputStream = new DataInputStream(socketClient.getInputStream());
			
			while(true) {
				int mouseX = dataInputStream.readInt();
				int mouseY = dataInputStream.readInt();
				
				
				try {
					Robot robot = new Robot();
//					robot.mouseMove(mouseX, mouseY);
					System.out.println("Move mouse from client X: " + mouseX + ", Y: " + mouseY);
				} catch(AWTException err) {
					err.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server() {
		GUI();
		ThreadSendImage();
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
