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
//				robot.mouseMove((int)mouseX, (int)mouseY);
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
			default:
//				throw new IllegalArgumentException("Unexpected value: ");
				break;
			}
		} catch (AWTException err) {
			err.printStackTrace();
		}
	}
	
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		try {
//			while (true) {
////				System.out.println("check");
////				try {
////					Robot robot = new Robot();
////					int command = dataInputStream.readInt();
//////					System.out.println("command");
////
////					switch (command) {
////					case -3: {
////						int keyCode = dataInputStream.readInt();
//////						robot.keyPress(keyCode);
//////						robot.keyRelease(keyCode);
////						System.out.println("KEY_PRESS : " + (char)keyCode);
////						break;
////					}
////					case -5: {
////						float mouseX = dataInputStream.readFloat();
////						float mouseY = dataInputStream.readFloat();
//////						robot.mouseMove((int)mouseX, (int)mouseY);
////						System.out.println("MouseX: " + mouseX + ", MouseY: " + mouseY);
////						break;
////					}
////					case -6: {
////						int mouseEvent = dataInputStream.readInt();
////						if (mouseEvent == MouseEvent.BUTTON1) {
//////							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//////				            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
////							System.out.println("MouseEvent_BUTTON1");
////						} else if (mouseEvent == MouseEvent.BUTTON3) {
//////							robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
//////				            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
////							System.out.println("MouseEvent_BUTTON3");
////						}
////						break;
////					}
////					default:
//////						throw new IllegalArgumentException("Unexpected value: ");
////						break;
////					}
////				} catch (AWTException err) {
////					err.printStackTrace();
////				}
//			}
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

}
