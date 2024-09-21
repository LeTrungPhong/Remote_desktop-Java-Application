package Server;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

public class CentralReader implements Runnable {
	
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private ReceiveEvents receiveEvents = null;
	private SendProcess sendProcess = null;
	
	public CentralReader(Socket socket) throws IOException {
		this.setSocket(socket);
		this.dataInputStream = new DataInputStream(socket.getInputStream());
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		receiveEvents = new ReceiveEvents(socket);
		sendProcess = new SendProcess(socket);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				int type = dataInputStream.readInt();
//				System.out.println("type: " + type);
				switch (type) {
				case -12: {
					receiveEvents.executeEventsByCommands(dataInputStream.readInt());
					break;
				}
				case -14: {
					sendProcess.sendProcess();
					break;
				}
				case -16: {
					System.out.println("Nhan thong bao khoi tao process");
					String ImageName = dataInputStream.readUTF();
					
					ProcessBuilder processBuilder = new ProcessBuilder(ImageName);
					Process process = processBuilder.start();
					
					int exitProcess = process.exitValue();
					System.out.println("Tien trinh ket thuc: " + exitProcess);
					break;
				}
				case -18: {
					Robot robot = new Robot();
					System.out.println("Nhan yeu chup man hinh");
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					Rectangle screenRect = new Rectangle(screenSize);
					BufferedImage screenCapture = robot.createScreenCapture(screenRect);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(screenCapture, "jpg", baos);
//					dataOutputStream.write(imageBytes);
					dataOutputStream.write(baos.toByteArray());
					dataOutputStream.flush();
					System.out.println("");
					break;
				}
				default:
//					throw new IllegalArgumentException("Unexpected value: " + );
					System.out.println("Not data");
					break;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
