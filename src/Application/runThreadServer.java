package Application;

import Server.Server;

public class runThreadServer implements Runnable {
	
	private volatile String password;
	private Server server;
	
	public runThreadServer(Server server, String password) {
		this.server = server;
		this.password = password;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		server = new Server(password);
	}
}