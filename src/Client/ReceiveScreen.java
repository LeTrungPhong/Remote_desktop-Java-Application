package Client;

import Client.Client;
import General.Commands;

import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ReceiveScreen implements Runnable {
	
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private Client client = null;
	private int widthScreenServer = -1;
	private int heightScreenServer = -1;
	private JLabel jlabelScreen;
	
	public ReceiveScreen(Socket socket, Client client) throws IOException {
		this.setSocket(socket);
		this.setClient(client);
		dataInputStream = new DataInputStream(socket.getInputStream());
		widthScreenServer = this.client.getWidthScreenServer();
		heightScreenServer = this.client.getHeightScreenServer();
		jlabelScreen = this.client.getLabelScreen();
		dataInputStream = new DataInputStream(socket.getInputStream());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				if(dataInputStream.readInt() == Commands.INFOR_SCREEN.getAbbrev()
						&& widthScreenServer != -1
						&& heightScreenServer != -1) {
					
					// Nhận dữ liệu từ server

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
					
//					Insets insets = this.client.getInsets();
//					int contentWidth = widthScreenServer - insets.left - insets.right;
//					int contentHeight = heightScreenServer - insets.bottom - insets.top;

					BufferedImage resizedImage = Client.resizeImage(receivedImage, widthScreenServer, heightScreenServer);

					if (resizedImage != null) {
						jlabelScreen.setIcon(new ImageIcon(resizedImage));
						jlabelScreen.revalidate();
						this.client.repaint();
					}
				}
			}
			
//			System.out.println(dataInputStream.readInt());
		} catch(IOException err) {
			err.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public DataInputStream getDataInputStream() {
		return this.dataInputStream;
	}
	
	public void setDataInputStream(DataInputStream dataInputStream) {
		this.dataInputStream = dataInputStream;
	}

}
