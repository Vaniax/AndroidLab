package service;

import java.util.List;
import simpleEntities.Location;
import simpleEntities.LoginData;
import simpleEntities.simpleAppointment;
import simpleEntities.simpleUser;

public interface ServiceInterface {
	
	//Account functions
	LoginData login(String passwort, String name);
	simpleUser createUser(String name, String password);
	
	//User actions functions
	boolean SendMessage(String SecurityToken, int userId, String message);
	simpleAppointment createAppointment(String SecurityToken, int userId, simpleAppointment appointment);	
	void inviteUsertoAppointment(String SecurityToken, int userId, int appointmentId, int inviteUserId);
	boolean visitAppointment(String SecurityToken, int userId, int appointmentId);
	boolean addFriendRequest(String SecurityToken, int userId, int friendId);
	boolean addFriendReply(String SecurityToken, int userId, int friendId, boolean accepted);

	
	//Information functions
	simpleUser getOwnData(String SecurityToken, int userId);
	List<simpleUser> GetFriends(String SecurityToken, int userId);
	Location GetFriendLocation(String SecurityToken, int userId, int friendId);
	List<Location> GetFriendLocations(String SecurityToken, int userId);
	List<String> GetMessages(String SecurityToken, int userId);
	List<simpleAppointment> GetNearAppointments(String SecurityToken, int userId, Location location);
	List<simpleAppointment> GetMyVisitingAppointments(String SecurityToken, int userId);

	//Passive update functions (called from android service)
	boolean UpdateLocation(String SecurityToken, int userId, Location location);

	
	
}
