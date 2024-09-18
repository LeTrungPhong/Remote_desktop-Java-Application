package Client;

import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;

import General.Commands;

public class SendEvents implements MouseListener, MouseMotionListener, KeyListener{

	private Socket socket = null;
	private JFrame jFrame = null;
	private DataOutputStream out = null;
	private float scale;
	
	public SendEvents(Socket socket, JFrame jFrame, float scale) throws IOException {
		this.setSocket(socket);
		this.setjFrame(jFrame);
		this.setScale(scale);
		out = new DataOutputStream(socket.getOutputStream());
		
		jFrame.addKeyListener(this);
		jFrame.addMouseListener(this);
		jFrame.addMouseMotionListener(this);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
//		System.out.println("Key Typed: " + e.getKeyChar());e
//		keyTyped = e.getKeyChar();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
//		System.out.println("KeyPressed :" + e.getKeyChar());
//		if (!keyHeld) {
//			keyCode = e.getKeyCode();
//
////			System.out.println("Phím đã được nhấn: " + e.getKeyChar());
//			keyHeld = true; // Đánh dấu là phím đang được giữ
//			keyHeldSendClient = true;
//		}
//		keyHeld = true;
//		System.out.println("1");
		
//		if(!keyHeld) { keyHeld = true; }
		
		try {
			out.writeInt(Commands.EVENTS.getAbbrev());
			out.writeInt(Commands.PRESS_KEY.getAbbrev());
			out.writeInt(e.getKeyCode());
			out.flush();
//			System.out.println("KeyPressed :" + e.getKeyCode());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
//		System.out.println("KeyReleased :" + e.getKeyChar());
//		if(e.getKeyChar() == keyTyped && keyHeld) {
////			keyHeld = true;
////			System.out.println("2");
//			
//		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
//		mouseX = e.getX();
//		mouseY = e.getY();
//        System.out.println("Mouse dragged to X: " + mouseX + ", Y: " + mouseY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
//		mouseX = e.getX();
//		mouseY = e.getY();
		try {
			Insets insets = jFrame.getInsets();
			out.writeInt(Commands.EVENTS.getAbbrev());
			out.writeInt(Commands.MOVE_MOUSE.getAbbrev());
			out.writeFloat((e.getX() - insets.left) / this.scale);
			out.writeFloat((e.getY() - insets.top) / this.scale);
			out.flush();
//			System.out.println("MouseMoved X: " + e.getX() / scale + ", Y: " + e.getY() / scale);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//        System.out.println("Mouse moved to X: " + mouseX + ", Y: " + mouseY);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
				try {
					Point point = e.getPoint();

					if (e.getButton() == MouseEvent.BUTTON1) {
//						System.out.println("You left click the mouse at " + point.x + " " + point.y);
//						click = 1;
						out.writeInt(Commands.EVENTS.getAbbrev());
						out.writeInt(Commands.CLICK_MOUSE.getAbbrev());
						out.writeInt(MouseEvent.BUTTON1);
						out.flush();
//						System.out.println("MouseClicked_BUTTON1");
					} else if (e.getButton() == MouseEvent.BUTTON3) {
//						System.out.println("You right click the mouse at " + point.x + " " + point.y);
//						click = 2;
						out.writeInt(Commands.EVENTS.getAbbrev());
						out.writeInt(Commands.CLICK_MOUSE.getAbbrev());
						out.writeInt(MouseEvent.BUTTON3);
						out.flush();
//						System.out.println("MouseClicked_BUTTON3");
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
//			System.out.println("You press the mouse at " + point.x + " " + point.y);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
//			System.out.println("You release the mouse at " + point.x + " " + point.y);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
//			System.out.println("You enter the window at " + point.x + " " + point.y);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Point point = e.getPoint();
//			System.out.println("You exit the window at " + point.x + " " + point.y);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public JFrame getjFrame() {
		return jFrame;
	}

	public void setjFrame(JFrame jFrame) {
		this.jFrame = jFrame;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}
