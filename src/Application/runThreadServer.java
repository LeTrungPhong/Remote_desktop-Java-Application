package Application;

import Server.Server;

public class runThreadServer implements Runnable {
	
	private String password;
	private Server server = null;
	
	public runThreadServer(String password) {
		this.password = password;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		server = new Server(password);
	}

}
