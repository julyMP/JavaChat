package ro.julymp.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 12345;

    Client client;

    public void startServer() {
	try {
	    ServerSocket serverSocket = new ServerSocket(PORT);
	    Socket clientSocket = serverSocket.accept();
	    this.client = new Client(clientSocket);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void sendMessage(String message) {
	client.sendMessage(message);
    }

    public String getMessage() {
	return client.getMessage();
    }

    public static void main(String[] args) {
	Server server = new Server();
	server.startServer();
	server.sendMessage("Hello!");
	System.out.println(server.getMessage());
	server.sendMessage("Bye!");
    }
}
