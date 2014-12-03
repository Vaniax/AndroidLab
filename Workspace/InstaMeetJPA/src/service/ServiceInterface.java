package service;

import java.util.List;
import java.util.Map;

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
	simpleAppointment CreateAppointments(String SecurityToken, int userId, simpleAppointment appointment);	
	boolean visitAppointment(String SecurityToken, int userId, int appointmentId);
	boolean addFriend(String SecurityToken, int userId, int friendId);
	/* Maybe split into addFriendRequest and AddFriendRepley (confirm/decline) */
	
	//Information functions
	simpleUser getOwnData(String SecurityToken, int userId);
//	Map<Integer, simpleUser> GetFriends(String SecurityToken, int userId);
	Location GetFriendLocation(String SecurityToken, int userId, int friendId);
	List<Location> GetFriendLocations(String SecurityToken, int userId);
	List<String> GetMessages(String SecurityToken, int userId);
//	Map<Double, simpleAppointment> GetNearAppointments(String SecurityToken, int userId, Location location);
//	Map<Integer, simpleAppointment> GetMyVisitingAppointments(String SecurityToken, int userId);

	//Passive update functions
	boolean UpdateLocation(String SecurityToken, int userId, Location location);

	
	
}
