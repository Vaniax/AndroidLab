package de.tubs.androidlab.instameet.ui.chat;

public class ChatMessageProxy {
	
	public enum DIRECTION {
		INCOMING,
		OUTGOING
	}
	
	private String message = new String();
	private DIRECTION direction; 
	
	public ChatMessageProxy(String message, DIRECTION direction) {
		this.message = message;
		this.direction = direction;
	}
	
	String getMessage() {
		return message;
	}
	
	DIRECTION getDirection() {
		return direction;
	}
}
