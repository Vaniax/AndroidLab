package de.tubs.androidlab.instameet.client.listener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class InboundListener {

	private List<AbstractInboundMessageListener> listeners = new ArrayList<AbstractInboundMessageListener>();
	
	public void addListener(AbstractInboundMessageListener listener)
	{
	    listeners.add(listener);
	}

	public void removeListener(AbstractInboundMessageListener listener)
	{
	    listeners.remove(listener);
	}
	
	public synchronized void notifyToken(String token) {
		for (AbstractInboundMessageListener l : listeners) {
			l.securityToken(token);
		}
	}

	public synchronized void notifyBool(boolean isTrue) {
		for (AbstractInboundMessageListener l : listeners) {
			l.bool(isTrue);
		}
	}
	
	public synchronized void notifyChatMessage(String message) {
		for (AbstractInboundMessageListener l : listeners) {
			l.chatMessage(message);
		}
	}

	public synchronized void notifyOwnData() {
		for (AbstractInboundMessageListener l : listeners) {
			l.ownData();
		}
	}
	
	public synchronized void notifyListFriends() {
		for (AbstractInboundMessageListener l : listeners) {
			l.listFriends();
		}
	}
}
