package ro.julymp.chat;

import java.io.Serializable;

/**
 * Message that will be used in communication
@author Marius-Pop Iuliana Dec 7, 2014
 */

public class Message implements Serializable{

    //protocol that identifies the type of the message and its structure
    private Protocol protocol;
    //message sent according to the protocol
    private String message;
    
    public Message(Protocol protocol, String message){
	this.protocol = protocol;
	this.message = message;
    }
    
    public Message(Protocol protocol){
	this(protocol, null);
    }
    
    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
