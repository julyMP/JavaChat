package ro.julymp.chat.message;

import java.io.Serializable;

/**
 * Message that will be used in communication
@author Marius-Pop Iuliana Dec 7, 2014
 */

public class Message implements Serializable{

    private static final long serialVersionUID = -5613561445539455194L;
    //protocol that identifies the type of the message and its structure
    private Protocol protocol;
    //message sent according to the protocol
    private String payload;
    
    
    public Message(Protocol protocol, String message){
	this.protocol = protocol;
	this.payload = message;
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String message) {
        this.payload = message;
    }
}
