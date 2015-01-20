package de.tubs.androidlab.instameet.ui.chat;

public class ChatMessage {
	
	public enum DIRECTION {
		INCOMING,
		OUTGOING
	}
	
	private String message = new String();
	private DIRECTION direction; 
	
	public ChatMessage(String message, DIRECTION direction) {
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
