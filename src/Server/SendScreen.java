package Server;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import General.Commands;

public class SendScreen implements Runnable {

	private Socket socket = null;
	private DataOutputStream dataOutputStream = null;
	private OutputStream outputStream = null;

	public SendScreen(Socket socket) throws IOException {
		this.setSocket(socket);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
		outputStream = socket.getOutputStream();
	}

	@Override
	public void run() {
		try {
			Robot robot = new Robot();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle screenRect = new Rectangle(screenSize);

			dataOutputStream.writeInt(Commands.SIZE_SERVER.getAbbrev());
			// gui kich thuoc man hinh server
			dataOutputStream.writeInt(screenSize.width);
			dataOutputStream.writeInt(screenSize.height);

			while (true) {
				// chup man hinh
				BufferedImage screenCapture = robot.createScreenCapture(screenRect);

				// sua lai kich thuoc
//				BufferedImage resizedImage = Server.resizeImage(screenCapture, getWidth(), getHeight());

				// luu anh thanh file
//				try {
//					String fileName = "screenshot" + screenShotCount + ".png";
//					ImageIO.write(screenCapture, "png", new File(fileName));
//					System.out.println("Saved screenshot: " + fileName);
//				} catch (IOException e) {
//                    e.printStackTrace();
//                }

				// Chuyển đổi ảnh thành byte array
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				ImageIO.write(screenCapture, "jpg", baos);
				// Phương thức ImageIO.write(image, "png", baos) trong Java được
				// sử dụng để ghi một đối tượng ảnh (BufferedImage) vào một luồng đầu
				// ra dưới định dạng ảnh nhất định (ở đây là PNG) và lưu vào
				// ByteArrayOutputStream
				// (một luồng đầu ra trong bộ nhớ).

				byte[] imageBytes = ByteBuffer.allocate(4).putInt(baos.size()).array();

				

				dataOutputStream.writeInt(Commands.INFOR_SCREEN.getAbbrev());
				dataOutputStream.write(imageBytes);
				dataOutputStream.write(baos.toByteArray());
				dataOutputStream.flush();

//				 cap nhat hinh anh tren JFrame
//				jLabelScreen.setIcon(new ImageIcon(resizedImage));
//				repaint();

				// dung 1 giay truoc khi chup lai
				
//				System.out.println("Gui screen thanh cong.");
				Thread.sleep(100);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
