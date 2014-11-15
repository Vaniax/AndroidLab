import java.util.List;


public interface ServiceInterface {
	//Login: Returns Security Token
	String login(String passwort, String name);
	
	//Basic
	List<Friend> GetFriends(String SecurityToken, int userId);
	List<Appointment> GetAppointments(String SecurityToken, int userId);
	Location GetFriendLocation(String SecurityToken, int userId, int friendId);
	List<Location> GetFriendLocations(String SecurityToken, int userId);
	List<String> GetMessages(String SecurityToken, int userId);
	boolean SendMessage(String SecurityToken, int userId, String message);
	boolean UpdateLocation(String SecurityToken, int userId, String location);
}
