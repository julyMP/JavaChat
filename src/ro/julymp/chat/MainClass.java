package ro.julymp.chat;

/**
@author Marius-Pop Iuliana Nov 21, 2014
 */

public class MainClass {

    public static void main(String[] args) {
	Server server = new Server();
	new Thread(server).start();
	Client client1 = new Client("client1", "127.0.0.1", Server.PORT);
	Client client2 = new Client("client2", "127.0.0.1", Server.PORT);
	new Thread(client1).start();
	new Thread(client2).start();

    }

}
