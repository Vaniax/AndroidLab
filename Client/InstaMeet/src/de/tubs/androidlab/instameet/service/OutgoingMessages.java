package de.tubs.androidlab.instameet.service;

import simpleEntities.SimpleAppointment;

public interface OutgoingMessages {
	public void login(String name, String password);

	public void createUser(String name, String password);

	public void sendMessage(String message, int friendID);
	public void fetchFriends();
	public void fetchOwnData();
	public void fetchNearAppointments();
	public void fetchVisitingAppointments();
	public void fetchUsersByName(String subString);
	public void createAppointment(SimpleAppointment app);

//	public void createAppointment(String SecurityToken, int userId,SimpleAppointment appointment);
//
//	public void inviteUsertoAppointment(String SecurityToken, int userId,int appointmentId, int inviteUserId);
//
//	public void visitAppointment(String SecurityToken, int userId,int appointmentId);
//
	public void addFriendRequest(String securityToken, String friendName);
//
//	public void addFriendReply(String SecurityToken, int userId, int friendId,boolean accepted);
//
//	public void getOwnData(String SecurityToken, int userId);
//
//	public void GetFriends(String SecurityToken, int userId);
//
//	public void GetFriendLocation(String SecurityToken, int userId, int friendId);
//
//	public void GetFriendLocations(String SecurityToken, int userId);
//
//	public void GetMessages(String SecurityToken, int userId);
//
//	public void GetNearAppointments(String SecurityToken, int userId,Location location);
//
//	public void GetMyVisitingAppointments(String SecurityToken, int userId);
//
//	public void getUsers(Set<Integer> ids);
//
//	public void UpdateLocation(String SecurityToken, int userId,Location location);
}
