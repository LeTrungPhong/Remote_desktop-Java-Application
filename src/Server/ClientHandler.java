package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Application.MainForm;
import General.Commands;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String serverPassword;
    private MainForm mainForm = null;

    public ClientHandler(Socket clientSocket, String password, MainForm mainForm) {
        this.clientSocket = clientSocket;
        this.serverPassword = password;
        this.mainForm = mainForm;
    }

    @Override
    public void run() {
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                if (dataInputStream.readInt() == Commands.REQUEST_CONNECT.getAbbrev()) {
                    String passwordClient = dataInputStream.readUTF();
                    System.out.println(passwordClient + " " + this.serverPassword);
                    if (passwordClient.equals(this.serverPassword)) {
                        System.out.println("Mat khau hop le");
                        dataOutputStream.writeInt(Commands.RESPONSE_CONNECT.getAbbrev());
                        dataOutputStream.writeBoolean(true);
                    } else {
                        System.out.println("Mat khau khong hop le");
                        dataOutputStream.writeInt(Commands.RESPONSE_CONNECT.getAbbrev());
                        dataOutputStream.writeBoolean(false);
                        clientSocket.close();
                    }
                    break;
                }
            }

            new Thread(new SendScreen(clientSocket)).start();
            new Thread(new CentralReader(clientSocket, ClientHandler.this.mainForm)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
