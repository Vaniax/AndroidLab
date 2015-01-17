package de.tubs.androidlab.instameet.client.listener;


/**
 * @author Vaniax
 * This abstract base class implements the {@link #InboundMessageListener} interface.
 * With this class a concrete listener is not required to implement unnecessary methods.
 */
public abstract class AbstractInboundMessageListener implements InboundMessageListener{

	public void securityToken(String token) {
		// Empty implementation
	}

	public void bool(boolean isTrue) {
		// Empty implementation
	}

	public void chatMessage(String message) {
		// Empty implementation
	}

	public void ownData() {
		// Empty implementation
	}
	
	public void listFriends() {
		// Empty implementation
	}
	
}
