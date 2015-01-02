package ro.julymp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ro.julymp.chat.message.Message;
import ro.julymp.chat.message.Protocol;

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
	    Object object = in.readObject();
	    if(object instanceof Message){
		return (Message)object;
	    }
	}catch (Exception e){
	    return null;
	}
	
	return null;
    }

    @Override
    public void run() {
	try {
	    this.socket = new Socket(host, port);
	    this.out = new ObjectOutputStream(socket.getOutputStream());
	    this.out.flush();
	    this.in = new ObjectInputStream(socket.getInputStream());
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	while (true) {
	    Message message = getMessage();
	    if (message != null) {
		switch (message.getProtocol()) {
		case IDENTIFY:
		    sendMessage(new Message(Protocol.IDENTIFY, this.userName));
		    sendMessage(new Message(Protocol.GET_USERS));
		    break;
		case GET_USERS:
		    String usersNameList = message.getPayload();
        	    System.out.println("Connected users: " +usersNameList);
		    break;
		case CONNECTED:
		    System.out.println("New Client connected: "
			    + message.getPayload());
		    break;
		case DISCONNECTED:
		    System.out.println("Client "
			    + message.getPayload() + " was disconnected.");
		    break;
		default:
		    System.out.println("CLIENT message: " + message.getProtocol() + ": " + message.getPayload());
		    break;
		}
	    }else{
		System.out.println("Server down: " + this.userName);
		break;
	    }

	}
    }

}
