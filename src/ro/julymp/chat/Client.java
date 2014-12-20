package ro.julymp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private String userName;
    private String host;
    private int port;
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public Client(String username, String host, int port) {
	this.userName = username;
	this.host = host;
	this.port = port;
    }

    public Client(Socket socket){
	this.socket = socket;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void sendMessage(Message message) {
	try {
	    out.writeObject(message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public Message getMessage() {
	try {
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
	try {
	    this.socket = new Socket(host, port);
	    this.out = new ObjectOutputStream(socket.getOutputStream());
	    this.in = new ObjectInputStream(socket.getInputStream());
	    Message identifyMessage = getMessage();
	    if(identifyMessage != null && identifyMessage.getProtocol() == Protocol.IDENTIFY){
		sendMessage(new Message(Protocol.IDENTIFY, this.userName));
	    }
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	while(true){
	    Message message = getMessage();
	    System.out.println(message.getMessage());
	}
    }

}
