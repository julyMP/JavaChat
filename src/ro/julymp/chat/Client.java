package ro.julymp.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    DataInputStream in;
    DataOutputStream out;

    public Client(Socket socket) {
	try {
	    this.in = new DataInputStream(socket.getInputStream());
	    this.out = new DataOutputStream(socket.getOutputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void sendMessage(String message) {
	try {
	    this.out.writeUTF(message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public String getMessage() {
	try {
	    return this.in.readUTF();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static void main(String[] args) {
	try {
	    Socket socket = new Socket("127.0.0.1", Server.PORT);
	    Client client = new Client(socket);
	    System.out.println(client.getMessage());
	    client.sendMessage("Goodbye!");
	    System.out.println(client.getMessage());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
