package ro.julymp.chat;

/**
 * A set of values to identify the type of the message
@author Marius-Pop Iuliana Dec 7, 2014
 */

public enum Protocol {

    //the message will contains client's user name
    IDENTIFY, 
    //the list of the clients
    GET_USERS, 
    //message to be send
    SEND_MESSAGE
    
}
