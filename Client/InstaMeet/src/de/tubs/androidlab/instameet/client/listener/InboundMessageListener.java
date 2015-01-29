package de.tubs.androidlab.instameet.client.listener;

import java.util.EventListener;
import java.util.List;

import simpleEntities.SimpleUser;

public interface InboundMessageListener extends EventListener {
	void securityToken(String token);
	void chatMessage();
	public void ownData();
	public void listFriends();
	public void listVisitingAppointments();
	public void listNearAppointments();
	public void listUsers(List<SimpleUser> users);
}
