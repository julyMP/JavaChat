package ro.julymp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {

    public static final int PORT = 12345;

    private Map<String, Socket> clients = new HashMap<>();

    public void startServer() {

	Thread connectionThread = new Thread() {
	    public void run() {
		try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
		    while (true) {
			Socket clientSocket;
			try {
			    clientSocket = serverSocket.accept();
			    sendMessage(clientSocket, new Message(Protocol.IDENTIFY));
			    Message identifyMessage = getMessage(clientSocket);
			    if (identifyMessage != null && identifyMessage.getProtocol() == Protocol.IDENTIFY) {
				clients.put(identifyMessage.getMessage(), clientSocket);
			    }
			    System.out.println("SERVER:" + identifyMessage.getMessage() + " connected!");
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }

	};
	connectionThread.start();

    }

    public void sendMessage(Socket socket, Message message) {
	try {
	    ObjectOutputStream out = new ObjectOutputStream(
		    socket.getOutputStream());
	    out.writeObject(message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public Message getMessage(Socket socket) {
	try {
	    ObjectInputStream in = new ObjectInputStream(
		    socket.getInputStream());
	    return (Message) in.readObject();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public void run() {
	startServer();
    }

}
