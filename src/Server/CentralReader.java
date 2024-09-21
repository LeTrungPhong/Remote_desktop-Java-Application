package Server;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.imageio.ImageIO;
import General.Commands;

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
					try {
						Process process = processBuilder.start();
						
						
						
//						int exitProcess = process.exitValue();
//						System.out.println("Tien trinh ket thuc: " + exitProcess);
						
					} catch (Exception e) {
						dataOutputStream.writeInt(Commands.ERROR_PROCESS.getAbbrev());
						dataOutputStream.writeUTF("Không thể khởi tạo chương trình:" + ImageName);
					}
					break;
				}
//				case -18: {
//					Robot robot = new Robot();
//					System.out.println("Nhan yeu chup man hinh");
//					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//					Rectangle screenRect = new Rectangle(screenSize);
//					BufferedImage screenCapture = robot.createScreenCapture(screenRect);
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					ImageIO.write(screenCapture, "jpg", baos);
////					dataOutputStream.write(imageBytes);
//					dataOutputStream.write(baos.toByteArray());
//					dataOutputStream.flush();
//					System.out.println("");
//					break;
//				}
				case -19: {
					System.out.println("Nhan thong bao tat process");
					String pid = dataInputStream.readUTF();
		            
		            if (!this.checkRunningProcess(pid)){
		            	dataOutputStream.writeInt(Commands.ERROR_PROCESS.getAbbrev());
		            	dataOutputStream.writeUTF("Không tìm thấy:" + pid);
		            	break;
		            }
		            String os = System.getProperty("os.name").toLowerCase();
		            String command;
		            if (os.contains("win")) {
		                command = "taskkill /PID " + pid;
		            } else {
		                command = "kill -9 " + pid;
		            }
		            Runtime.getRuntime().exec(command);
		            
//		            if (this.checkRunningProcess(pid)) {
//		                dataOutputStream.writeInt(Commands.ERROR_PROCESS.getAbbrev());
//		                dataOutputStream.writeUTF("Không thể tắt process với PID: " + pid);
//		            } 
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
	
	Boolean checkRunningProcess(String pid) {
		String os = System.getProperty("os.name").toLowerCase();
	    String command;
	    if (os.contains("win")) {
	        command = "tasklist /FI \"PID eq " + pid + "\"";
	    } else {
	        command = "ps -p " + pid;
	    }

	    try {
	        Process checkProcess = Runtime.getRuntime().exec(command);

	        BufferedReader reader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
	        String line;
	        boolean processExists = false;

	        while ((line = reader.readLine()) != null) {
	            if (line.contains(pid)) {
	                processExists = true;
	                break;
	            }
	        }

	        checkProcess.waitFor(); // Chờ lệnh hoàn thành
	        return processExists;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
