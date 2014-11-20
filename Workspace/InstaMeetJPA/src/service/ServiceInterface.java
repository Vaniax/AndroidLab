package service;

import java.util.List;

import entities.Appointment;
import entities.LoginData;
import entities.User;
import entities.Location;

public interface ServiceInterface {
	//Login: Returns Security Token
	LoginData login(String passwort, String name);
	
	//Basic
	User getOwnData(String SecurityToken, int userId);
	
	List<User> GetFriends(String SecurityToken, int userId);
	Location GetFriendLocation(String SecurityToken, int userId, int friendId);
	List<Location> GetFriendLocations(String SecurityToken, int userId);
	boolean UpdateLocation(String SecurityToken, int userId, Location location);

	//Appointments
	Appointment CreateAppointments(String SecurityToken, int userId, Appointment appointment);
	List<Appointment> GetAppointments(String SecurityToken, int userId);
	
	List<String> GetMessages(String SecurityToken, int userId);
	boolean SendMessage(String SecurityToken, int userId, String message);
	
}
