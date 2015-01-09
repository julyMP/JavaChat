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

    ClientAction clientAction;
    
    public Client(String username, String host, int port) {
	this.userName = username;
	this.host = host;
	this.port = port;
    }
    
    public Client(String username, String host, int port, ClientAction clientAction) {
	this(username, host, port);
	this.clientAction = clientAction;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public ClientAction getClientAction() {
        return clientAction;
    }

    public void setClientAction(ClientAction clientAction) {
        this.clientAction = clientAction;
    }
    
    public void sendMessage(Message message) {
	try {
	    out.writeObject(message);
	} catch (IOException e) {
	    if(this.clientAction != null){
		this.clientAction.onExeption(e);
	    }
	}
    }

    
    
    public Message getMessage() {
	try {
	    Object object = in.readObject();
	    if (object instanceof Message) {
		return (Message) object;
	    }
	} catch (Exception e) {
	    return null;
	}

	return null;
    }

    @Override
    public void run(){
	try {
	    this.socket = new Socket(host, port);
	    this.out = new ObjectOutputStream(socket.getOutputStream());
	    this.out.flush();
	    this.in = new ObjectInputStream(socket.getInputStream());
	    if(this.clientAction != null){
		this.clientAction.afterStart();
	    }
	} catch (UnknownHostException e) {
	    if(this.clientAction != null){
		this.clientAction.onExeption(e);
	    }
	} catch (IOException e) {
	    if(this.clientAction != null){
		this.clientAction.onExeption(e);
	    }
	}
	while (true) {
	    Message message = getMessage();
	    if (message != null) {
		switch (message.getProtocol()) {
		case IDENTIFY:
		    sendMessage(new Message(Protocol.IDENTIFY, this.userName));
		    break;
		case INVALID:
		    if(this.clientAction != null){
			this.clientAction.onExeption(new Exception("Invalid user"));
		    }
		    return;
		case GET_USERS:
		    String usersNameList = message.getPayload();
		    List<String> users = parseUsersList(usersNameList);
		    if(this.clientAction != null){
			this.clientAction.getUsers(users);
		    }
		    break;
		case CONNECTED:
		    if(this.clientAction != null){
			this.clientAction.connect(message.getPayload());
		    }
		    break;
		case DISCONNECTED:
		    if(this.clientAction != null){
			this.clientAction.disconnect(message.getPayload());
		    }
		    break;
		case SEND_MESSAGE:
		    String sender = message.getPayload().substring(1,message.getPayload().indexOf("]"));
	            String mess = message.getPayload().substring(message.getPayload().indexOf("]") + 1);
	            if(this.clientAction != null){
			this.clientAction.receiveMessage(sender, mess);
		    }
	            break;
		default:
		    System.out.println("CLIENT message: " + message.getProtocol() + ": " + message.getPayload());
		    break;
		}
	    } else {
		System.out.println("Server down: " + this.userName);
		break;
	    }

	}
    }

    private List<String> parseUsersList(String users) {
	List<String> usersList = new ArrayList<String>();
        //[jjjjj,jjjkkjn,kiouh]
	String[] parts = users.split("[\\[\\],]");
	for(String part:parts){
	    if(part!=null && !part.isEmpty()){
		usersList.add(part.trim());
	    }
	}
	return usersList;
    }

}
