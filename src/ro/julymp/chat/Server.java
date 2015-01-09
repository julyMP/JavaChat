package ro.julymp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ro.julymp.chat.message.Message;
import ro.julymp.chat.message.Protocol;

public class Server implements Runnable {

    public static final int PORT = 12345;
    public static Map<String, ClientListener> clients = Collections.synchronizedMap(new HashMap<String, ClientListener>());

    public void startServer() {

	Thread connectionThread = new Thread() {
	    public void run() {
		try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
		    while (true) {
			Socket clientSocket;
			try {
			    System.out.println("SERVER: waiting for client connection...");
			    clientSocket = serverSocket.accept();
			    System.out.println("SERVER: client connected.");
			    ClientListener clientListener = new ClientListener(clientSocket);
			    new Thread(clientListener).start();
			} catch (IOException e) {
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

    @Override
    public void run() {
	startServer();
    }

    public synchronized void addClient(String username, ClientListener clientListener) {
	clients.put(username, clientListener);
    }

    public synchronized void removeClient(String username) {
	clients.remove(username);
    }

    public synchronized void sendConnectedMessage(String username) {
	Message connectedMessage = new Message(Protocol.CONNECTED, username);
	for (Entry<String, ClientListener> entry : clients.entrySet()) {
	    if (!entry.getKey().equals(username)) {
		entry.getValue().sendMessage(connectedMessage);
	    }
	}
    }

    public synchronized void sendDisconnectedMessage(String username) {
	Message disconnectedMassage = new Message(Protocol.DISCONNECTED, username);
	removeClient(username);
	for (Entry<String, ClientListener> entry : clients.entrySet()) {
	    if (!entry.getKey().equals(username)) {
		entry.getValue().sendMessage(disconnectedMassage);
	    }
	}

    }

    private class ClientListener implements Runnable {
	private final Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String username;

	public ClientListener(Socket socket) {
	    this.socket = socket;
	    try {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.out.flush();
		this.in = new ObjectInputStream(socket.getInputStream());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	public void sendMessage(Message message) {
	    try {
		out.writeObject(message);
	    } catch (IOException e) {
		// sendDisconnectedMessage(username);
	    }
	}

	public Message getMessage(Socket socket) {
	    try {
		return (Message) in.readObject();
	    } catch (Exception e) {
		System.out.println("client " + username + " disconnected");
		sendDisconnectedMessage(username);
		return null;
	    }

	}

	@Override
	public void run() {
	    Message im = new Message(Protocol.IDENTIFY);
	    sendMessage(im);
	    while (true) {
		Message message = getMessage(this.socket);
		if (message != null) {
		    switch (message.getProtocol()) {
		    case IDENTIFY:
			String username = message.getPayload();
			if (!clients.containsKey(username)) {
			    sendConnectedMessage(username);
			    addClient(username, this);
			    this.username = username;
			    System.out.println("Client " + username + " is now connected.");
			    break;
			} else {
			    sendMessage(new Message(Protocol.INVALID));
			    return;
			}

		    case GET_USERS:
			List<String> users = new ArrayList<String>(clients.size());
			users.addAll(clients.keySet());
			message.setPayload(users.toString());
			sendMessage(message);
			break;
		    case SEND_MESSAGE:
			// [receiver]message
			String receiver = message.getPayload().substring(1, message.getPayload().indexOf("]"));
			String mess = message.getPayload().substring(message.getPayload().indexOf("]") + 1);
			clients.get(receiver).sendMessage(new Message(Protocol.SEND_MESSAGE, "[" + this.username + "]" + mess));
			break;
		    case DISCONNECTED:
			sendDisconnectedMessage(this.username);
			break;
		    default:
			System.out.println(message.getProtocol() + ":" + message.getPayload());
			break;
		    }
		} else {
		    break;
		}
	    }
	}
    }

    public static void main(String[] args) {
	Server server = new Server();
	new Thread(server).start();
    }

}
