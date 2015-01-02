package ro.julymp.chat;

/**
@author Marius-Pop Iuliana Nov 21, 2014
 */

public class MainClass {

    public static void main(String[] args) {
//	Server server = new Server();
//	new Thread(server).start();
	Client client1 = new Client("client3", "127.0.0.1", Server.PORT);
	Client client2 = new Client("client4", "127.0.0.1", Server.PORT);
	new Thread(client1).start();
	new Thread(client2).start();
//	try{
//	    Thread.sleep(5000);
//	}catch(InterruptedException e){
//	    e.printStackTrace();
//	}
//	client1.sendMessage(new Message(Protocol.GET_USERS, "give me the users please! :D"));
    }

}
