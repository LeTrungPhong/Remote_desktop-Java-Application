package Client.BLL;

import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;

import General.Commands;

public class SendEvents implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

	private Socket socket = null;
	private JFrame jFrame = null;
	private DataOutputStream dataOutputStream = null;
	private float scale;

	public SendEvents(Socket socket, JFrame jFrame, float scale) throws IOException {
		this.setSocket(socket);
		this.setjFrame(jFrame);
		this.setScale(scale);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());

		jFrame.addKeyListener(this);
		jFrame.addMouseListener(this);
		jFrame.addMouseMotionListener(this);
		jFrame.addMouseWheelListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		try {
			dataOutputStream.writeInt(Commands.EVENTS.getAbbrev());
			dataOutputStream.writeInt(Commands.PRESS_KEY.getAbbrev());
			dataOutputStream.writeInt(e.getKeyCode());
			dataOutputStream.flush();
//			System.out.println("KeyPressed :" + e.getKeyCode());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			Insets insets = jFrame.getInsets();
			dataOutputStream.writeInt(Commands.EVENTS.getAbbrev());
			dataOutputStream.writeInt(Commands.MOVE_MOUSE.getAbbrev());
			dataOutputStream.writeFloat((e.getX() - insets.left) / this.scale);
			dataOutputStream.writeFloat((e.getY() - insets.top) / this.scale);
			dataOutputStream.flush();
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
				dataOutputStream.writeInt(Commands.EVENTS.getAbbrev());
				dataOutputStream.writeInt(Commands.CLICK_MOUSE.getAbbrev());
				dataOutputStream.writeInt(MouseEvent.BUTTON1);
				dataOutputStream.flush();
//						System.out.println("MouseClicked_BUTTON1");
			} else if (e.getButton() == MouseEvent.BUTTON3) {
//						System.out.println("You right click the mouse at " + point.x + " " + point.y);
				dataOutputStream.writeInt(Commands.EVENTS.getAbbrev());
				dataOutputStream.writeInt(Commands.CLICK_MOUSE.getAbbrev());
				dataOutputStream.writeInt(MouseEvent.BUTTON3);
				dataOutputStream.flush();
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

		try {
			int notches = e.getWheelRotation();
			if (notches < 0) {
//				System.out.println("Lăn chuột lên ...");
			} else {
//				System.out.println("lăn chuột xuống ...");
			}
			dataOutputStream.writeInt(Commands.EVENTS.getAbbrev());
			dataOutputStream.writeInt(Commands.MOUSE_WHEEL.getAbbrev());
			dataOutputStream.writeInt(notches);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
