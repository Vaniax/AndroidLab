package de.tubs.androidlab.instameet.ui.chat;

public class ChatMessageProxy {
	
	public enum DIRECTION {
		INCOMING,
		OUTGOING
	}
	
	private String message = new String();
	private DIRECTION direction; 
	private long time;
	
	public ChatMessageProxy(String message, DIRECTION direction, long time) {
		this.message = message;
		this.direction = direction;
	}
	
	String getMessage() {
		return message;
	}
	
	DIRECTION getDirection() {
		return direction;
	}
	
	long getTime() {
		return time;
	}
}
