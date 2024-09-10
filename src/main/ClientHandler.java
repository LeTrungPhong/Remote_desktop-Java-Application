package main;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ClientHandler implements Runnable {

	private Socket socket;
	private PrintWriter out;
	private BufferedImage outImage;
	private BufferedReader in;
	private OutputStream outputStream;
	private String clientName;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			
			int width = Toolkit.getDefaultToolkit().getScreenSize().width;
			int height = Toolkit.getDefaultToolkit().getScreenSize().height;
			
			try {
				Robot robot = new Robot();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Rectangle screenRect = new Rectangle(screenSize);
				
				int screenShotCount = 0;
				
				while(true) {
					// chup man hinh
					BufferedImage screenCapture = robot.createScreenCapture(screenRect);
					
					// sua lai kich thuoc
					BufferedImage resizedImage = Server.resizeImage(screenCapture, width, height);

					// Chuyển đổi ảnh thành byte array
		            ByteArrayOutputStream baos = new ByteArrayOutputStream();
		            
		            ImageIO.write(resizedImage, "jpg", baos);
		            // Phương thức ImageIO.write(image, "png", baos) trong Java được 
		            // sử dụng để ghi một đối tượng ảnh (BufferedImage) vào một luồng đầu 
		            // ra dưới định dạng ảnh nhất định (ở đây là PNG) và lưu vào ByteArrayOutputStream 
		            // (một luồng đầu ra trong bộ nhớ).
		            
		            byte[] imageBytes = ByteBuffer.allocate(4).putInt(baos.size()).array();
		            
		            outputStream = socket.getOutputStream();
		            
		            outputStream.write(imageBytes);
		            outputStream.write(baos.toByteArray());
		            outputStream.flush();
					
					// dung 1 giay truoc khi chup lai 
					Thread.sleep(1000);
				} 
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Loi gui file");
			}
		} catch(IOException e) {
			System.out.println("Error handling client: " + e.getMessage());
		} finally {
			try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}
	
	public void sendMessage(String message) {
		out.println(message);
	}
}
