package service;

import java.util.List;
import java.util.Set;

import simpleEntities.Location;
import simpleEntities.LoginData;
import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;

public interface ServiceInterface {
	
	//Account functions
	LoginData login(String name, String passwort);
	SimpleUser createUser(String name, String password);
	
	//User actions functions
	boolean SendMessage(String SecurityToken, int userId, String message);
	SimpleAppointment createAppointment(String SecurityToken, int userId, SimpleAppointment appointment);	
	void inviteUsertoAppointment(String SecurityToken, int userId, int appointmentId, int inviteUserId);
	boolean visitAppointment(String SecurityToken, int userId, int appointmentId);
	boolean addFriendRequest(String SecurityToken, int userId, int friendId);
	boolean addFriendReply(String SecurityToken, int userId, int friendId, boolean accepted);

	
	//Information functions
	SimpleUser getOwnData(String SecurityToken, int userId);
	List<SimpleUser> GetFriends(String SecurityToken, int userId);
	Location GetFriendLocation(String SecurityToken, int userId, int friendId);
	List<Location> GetFriendLocations(String SecurityToken, int userId);
	List<String> GetMessages(String SecurityToken, int userId);
	List<SimpleAppointment> GetNearAppointments(String SecurityToken, int userId, Location location);
	List<SimpleAppointment> GetMyVisitingAppointments(String SecurityToken, int userId);
	
	List<SimpleUser> getUsers(Set<Integer> ids);
	List<SimpleUser> getUsersByName(String subName);
	
	//Passive update functions (called from android service)
	boolean UpdateLocation(String SecurityToken, int userId, Location location);

	
	
}
