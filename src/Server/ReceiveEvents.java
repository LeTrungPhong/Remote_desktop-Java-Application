package Server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import General.Commands;

public class ReceiveEvents {
	
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private float postX = 0;
	private float postY = 0;

	public ReceiveEvents(Socket socket) throws IOException {
		// TODO Auto-generated constructor stub
		this.setSocket(socket);
		dataInputStream = new DataInputStream(socket.getInputStream());
	}
	
	public void executeEventsByCommands(int command) throws IOException {
		try {
			Robot robot = new Robot();

			switch (command) {
			case -3: {
				int keyCode = dataInputStream.readInt();
//				robot.keyPress(keyCode);
//				robot.keyRelease(keyCode);
				System.out.println("KEY_PRESS : " + (char)keyCode);
				break;
			}
			case -5: {
				float mouseX = dataInputStream.readFloat();
				float mouseY = dataInputStream.readFloat();
				if(mouseX != postX && mouseY != postY) {
//					robot.mouseMove((int)mouseX, (int)mouseY);
					postX = mouseX;
					postY = mouseY;
				}
				System.out.println("MouseX: " + mouseX + ", MouseY: " + mouseY);
				break;
			}
			case -6: {
				int mouseEvent = dataInputStream.readInt();
				if (mouseEvent == MouseEvent.BUTTON1) {
//					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					System.out.println("MouseEvent_BUTTON1");
				} else if (mouseEvent == MouseEvent.BUTTON3) {
//					robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
//		            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
					System.out.println("MouseEvent_BUTTON3");
				}
				break;
			}
			case -50: {
				int notches = dataInputStream.readInt();
//				robot.mouseWheel(notches);
				System.out.println("MouseWheel: " + notches);
				break;
			}
			case -26: {
				Runtime.getRuntime().exec("shutdown -s -t 0");
				break;
			}
			default:
//				throw new IllegalArgumentException("Unexpected value: ");
				break;
			}
		} catch (AWTException err) {
			err.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
