package de.tubs.androidlab.instameet.client.listener;

import java.util.List;

import simpleEntities.SimpleUser;


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

	public void chatMessage() {
		// Empty implementation
	}

	public void ownData() {
		// Empty implementation
	}
	
	public void listFriends() {
		// Empty implementation
	}
	public void listVisitingAppointments() {
		//Empty implementation
	}
	public void listNearAppointments() {
		//Empty implementation
	}
	public void listUsers(List<SimpleUser> users) {
		//Empty implementation
	}
	public void appointment() {
		// Empty implementation
	}

	public void friendRequest() {
		// Empty implementation
		
	}
	
}
