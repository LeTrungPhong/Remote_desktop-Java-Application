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
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import Application.MainForm;
import General.Commands;

public class CentralReader implements Runnable {
	
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private ReceiveEvents receiveEvents = null;
	private SendProcess sendProcess = null;
	private boolean checkConnect = true;
	private MainForm mainForm = null;
	private DisconnectForm disconnectForm = null;
	
	public CentralReader(Socket socket, MainForm mainForm, DisconnectForm disconnectForm) throws IOException {
		this.setSocket(socket);
		this.dataInputStream = new DataInputStream(socket.getInputStream());
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		this.mainForm = mainForm;
		this.disconnectForm = disconnectForm;
		receiveEvents = new ReceiveEvents(socket);
		sendProcess = new SendProcess(socket);
		
		JOptionPane.showMessageDialog(
			    this.mainForm, 
			    "Máy tính đang được điều khiển!", 
			    "Thông báo",
			    JOptionPane.INFORMATION_MESSAGE
			);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(checkConnect) {
			try {
				int type = dataInputStream.readInt();
				Commands commands = Commands.fromAbbrev(type);
//				System.out.println("type: " + type);
				switch (commands) {
				case EVENTS: {
					receiveEvents.executeEventsByCommands(dataInputStream.readInt());
					break;
				}
				case REQUEST_PROCESS: {
					sendProcess.sendProcess();
					break;
				}
				case REQUEST_START_PROCESS: {
					
					System.out.println("Nhan thong bao khoi tao process");
					String ImageName = dataInputStream.readUTF();
					
					ProcessBuilder processBuilder = new ProcessBuilder(ImageName);
					try {
						Process process = processBuilder.start();
						
//						int exitProcess = process.exitValue();
//						System.out.println("Tien trinh ket thuc: " + exitProcess);
						Thread.sleep(1000);
						sendProcess.sendProcess();
					} catch (Exception e) {
						dataOutputStream.writeInt(Commands.ERROR_PROCESS.getAbbrev());
						dataOutputStream.writeUTF("Không thể khởi tạo chương trình:" + ImageName);
					}
					break;
				}
				case REQUEST_STOP_PROCESS: {
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
		            Thread.sleep(1000);
		            sendProcess.sendProcess();
//		            if (this.checkRunningProcess(pid)) {
//		                dataOutputStream.writeInt(Commands.ERROR_PROCESS.getAbbrev());
//		                dataOutputStream.writeUTF("Không thể tắt process với PID: " + pid);
//		            } 
		            break;
				}
				case REQUEST_SCREEN_SHOT: {
					Robot robot = new Robot();
					System.out.println("Nhan yeu chup man hinh");
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					Rectangle screenRect = new Rectangle(screenSize);
					BufferedImage screenCapture = robot.createScreenCapture(screenRect);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(screenCapture, "jpg", baos);
					
					byte[] imageBytes = ByteBuffer.allocate(4).putInt(baos.size()).array();
					
					dataOutputStream.writeInt(Commands.RESPONSE_SCREEN_SHOT.getAbbrev());
					dataOutputStream.write(imageBytes);
					dataOutputStream.write(baos.toByteArray());
					dataOutputStream.flush();
					System.out.println("");
					break;
				}
				case REQUEST_START_KEYLOGGER: {
					new Thread(new Keylogger(socket)).start();
					break;
				}
				case REQUEST_APP_RUNNING: {
					break;
				}
				case REQUEST_DISCONNECT: {
					JOptionPane.showMessageDialog(
						    this.mainForm, 
						    "Client da ngat ket noi!", 
						    "Thông báo",
						    JOptionPane.INFORMATION_MESSAGE
						);
					checkConnect = false;
					this.mainForm.setVisible(true);
					this.disconnectForm.dispose();
					break;
				}
				default:
					break;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage() + "1");
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
