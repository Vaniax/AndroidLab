package de.tubs.androidlab.instameet.service;

import de.tubs.androidlab.instameet.server.protobuf.Messages.*;

public interface ReceivedMessageCallbacks {

	public void chatMessage(ChatMessage msg);
	public void listFriends(ListFriends msg);
	public void listChatMessages(ListChatMessages msg);
	public void securityToken(SecurityToken token);
	public void bool(BoolReply bool);
	public void ownData(OwnData ownData);
}
