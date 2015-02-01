package de.tubs.androidlab.instameet.service;

import de.tubs.androidlab.instameet.server.protobuf.Messages.ListUsers;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SimpleUser;
import de.tubs.androidlab.instameet.server.protobuf.Messages.*;

public interface ReceivedMessageCallbacks {

	public void chatMessage(ChatMessage msg);
	public void listFriends(ListFriends msg);
	public void listChatMessages(ListChatMessages msg);
	public void securityToken(SecurityToken token);
	public void bool(BoolReply bool);
	public void ownData(OwnData ownData);
	public void listNearAppointments(ListNearestAppointments msg);
	public void listVisitingAppointments(ClientResponse msg);
	public void listUsers(ListUsers listUsers);
	public void simpleAppointment(SimpleAppointment app);
	public void addFriendRequest(SimpleUser user);
}
