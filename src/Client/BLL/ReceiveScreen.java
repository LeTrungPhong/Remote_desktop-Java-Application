package Client.BLL;

import General.Commands;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Client.View.ClientForm;

public class ReceiveScreen {
	
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private ClientForm client = null;
	private int widthScreenServer = -1;
	private int heightScreenServer = -1;
	private JLabel jlabelScreen;
	
	public ReceiveScreen(Socket socket, ClientForm client) throws IOException {
		this.setSocket(socket);
		this.setClient(client);
		dataInputStream = new DataInputStream(socket.getInputStream());
		widthScreenServer = this.client.getWidthScreenServer();
		heightScreenServer = this.client.getHeightScreenServer();
		jlabelScreen = this.client.getLabelScreen();
		dataInputStream = new DataInputStream(socket.getInputStream());
	}
	public void receiveScreenByCommands(int command) throws IOException {
		if(command == Commands.INFOR_SCREEN.getAbbrev()
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
			
//			Insets insets = this.client.getInsets();
//			int contentWidth = widthScreenServer - insets.left - insets.right;
//			int contentHeight = heightScreenServer - insets.bottom - insets.top;

			BufferedImage resizedImage = resizeImage(receivedImage, widthScreenServer, heightScreenServer);

			if (resizedImage != null) {
				jlabelScreen.setIcon(new ImageIcon(resizedImage));
				jlabelScreen.revalidate();
				this.client.repaint();
			}
		}
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

//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		try {
//			while (true) {
//				
//			}
//			
////			System.out.println(dataInputStream.readInt());
//		} catch(IOException err) {
//			err.printStackTrace();
//		}
//	}

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
	
	public DataInputStream getDataInputStream() {
		return this.dataInputStream;
	}
	
	public void setDataInputStream(DataInputStream dataInputStream) {
		this.dataInputStream = dataInputStream;
	}

}
