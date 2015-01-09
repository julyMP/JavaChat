package ro.julymp.chat;

import java.util.List;

import ro.julymp.chat.message.Message;
import ro.julymp.chat.message.Protocol;

/**
@author Marius-Pop Iuliana Nov 21, 2014
 */

public class MainClass {

    public static void main(String[] args) {
	Server server = new Server();
	new Thread(server).start();
//	ClientAction clientAction1 = new ClientAction() {
//	    
//	    @Override
//	    public void receiveMessage(String sender, String message) {
//		System.out.println("mesaj primit de la " + sender + ":" + message);
//		
//	    }
//	    
//	    @Override
//	    public void getUsers(List<String> users) {
//		// TODO Auto-generated method stub
//		
//	    }
//	    
//	    @Override
//	    public void disconnect(String user) {
//		// TODO Auto-generated method stub
//		
//	    }
//	    
//	    @Override
//	    public void connect(String user) {
//		// TODO Auto-generated method stub
//		
//	    }
//
//	    @Override
//	    public void onExeption(Exception e) {
//		// TODO Auto-generated method stub
//		
//	    }
//
//	    @Override
//	    public void afterStart() {
//		// TODO Auto-generated method stub
//		
//	    }
//	};
//	Client client1 = new Client("client3", "127.0.0.1", Server.PORT);
//	Client client2 = new Client("client4", "127.0.0.1", Server.PORT, clientAction1);
//	new Thread(client1).start();
//	new Thread(client2).start();
//	try{
//	    Thread.sleep(5000);
//	}catch(InterruptedException e){
//	    e.printStackTrace();
//	}
//	//client1.sendMessage(new Message(Protocol.GET_USERS, "give me the users please! :D"));
//	client1.sendMessage(new Message(Protocol.SEND_MESSAGE,"[client4]mesaj de test"));
    }

}
