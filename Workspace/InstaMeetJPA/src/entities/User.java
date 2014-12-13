package entities;

import java.io.Serializable;

import javax.persistence.*;

import simpleEntities.SimpleUser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the User database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
	@NamedQuery(name="User.login", query="SELECT a FROM User a WHERE a.username = :name and a.password = :password"),
	@NamedQuery(name="User.findId", query="SELECT a FROM User a WHERE a.id = :id"),
	@NamedQuery(name="User.findName", query="SELECT a FROM User a WHERE a.username = :name")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="latest_location_update")
	private Timestamp latestLocationUpdate;

	private double lattitude;

	private double longitude;

	private String password;

	private String salt;

	private String username;

	//bi-directional many-to-one association to Appointment
	@OneToMany(mappedBy="hoster")
	private List<Appointment> hostedAppointments;

	//bi-directional many-to-one association to ChatMessage
	@OneToMany(mappedBy="sender")
	private List<ChatMessage> sentMessages;

	//bi-directional many-to-one association to ChatMessage
	@OneToMany(mappedBy="receiver")
	private List<ChatMessage> receivedMessages;

	//bi-directional many-to-one association to Friend
	@OneToMany(mappedBy="user")
	private List<ConfirmedFriend> confirmedFriendShips;

	/** Friendships requests from me**/
	//bi-directional many-to-one association to Friend
	@OneToMany(mappedBy="user")
	private List<UnconfirmedFriend> unconfirmedFriendShips;
	
	/** Friendships requests to me**/
	@OneToMany(mappedBy="friend")
	private List<UnconfirmedFriend> requestedFriendShips;

	//bi-directional many-to-one association to Visitor
	@OneToMany(mappedBy="user")
	private List<Visitor> confirmedVisitors;
	
	@OneToMany(mappedBy="user")
	private List<Visitor> invitedVisitors;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getLatestLocationUpdate() {
		return this.latestLocationUpdate;
	}

	public void setLatestLocationUpdate(Timestamp latestLocationUpdate) {
		this.latestLocationUpdate = latestLocationUpdate;
	}

	public double getLattitude() {
		return this.lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Appointment> getHostedAppointments() {
		return this.hostedAppointments;
	}

	public void setHostedAppointments(List<Appointment> hostedAppointments) {
		this.hostedAppointments = hostedAppointments;
	}

	public Appointment addHostedAppointment(Appointment hostedAppointment) {
		getHostedAppointments().add(hostedAppointment);
		hostedAppointment.setHoster(this);

		return hostedAppointment;
	}

	public Appointment removeHostedAppointment(Appointment hostedAppointment) {
		getHostedAppointments().remove(hostedAppointment);
		hostedAppointment.setHoster(null);

		return hostedAppointment;
	}

	public List<ChatMessage> getSentMessages() {
		return this.sentMessages;
	}

	public void setSentMessages(List<ChatMessage> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public ChatMessage addSentMessage(ChatMessage sentMessage) {
		getSentMessages().add(sentMessage);
		sentMessage.setSender(this);

		return sentMessage;
	}

	public ChatMessage removeSentMessage(ChatMessage sentMessage) {
		getSentMessages().remove(sentMessage);
		sentMessage.setSender(null);

		return sentMessage;
	}

	public List<ChatMessage> getReceivedMessages() {
		return this.receivedMessages;
	}

	public void setReceivedMessages(List<ChatMessage> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public ChatMessage addReceivedMessage(ChatMessage receivedMessage) {
		getReceivedMessages().add(receivedMessage);
		receivedMessage.setReceiver(this);

		return receivedMessage;
	}

	public ChatMessage removeReceivedMessage(ChatMessage receivedMessage) {
		getReceivedMessages().remove(receivedMessage);
		receivedMessage.setReceiver(null);

		return receivedMessage;
	}

	public List<ConfirmedFriend> getConfirmedFriendShips() {
		return this.confirmedFriendShips;
	}

	public void setConfirmedFriendShips(List<ConfirmedFriend> confirmedFriendShips) {
		this.confirmedFriendShips = confirmedFriendShips;
	}

	public ConfirmedFriend addConfirmedFriendShip(ConfirmedFriend confirmedFriendShip) {
		getConfirmedFriendShips().add(confirmedFriendShip);
		confirmedFriendShip.setUser(this);

		return confirmedFriendShip;
	}

	public ConfirmedFriend removeConfirmedFriendShip(ConfirmedFriend confirmedFriendShip) {
		getConfirmedFriendShips().remove(confirmedFriendShip);
		confirmedFriendShip.setUser(null);

		return confirmedFriendShip;
	}

	public List<UnconfirmedFriend> getUnconfirmedFriendShips() {
		return this.unconfirmedFriendShips;
	}
	

	public void setUnconfirmedFriendShips(List<UnconfirmedFriend> unconfirmedFriendShips) {
		this.unconfirmedFriendShips = unconfirmedFriendShips;
	}

	public UnconfirmedFriend addUnconfirmedFriendShip(UnconfirmedFriend unconfirmedFriendShip) {
		getUnconfirmedFriendShips().add(unconfirmedFriendShip);
		unconfirmedFriendShip.setUser(this);

		return unconfirmedFriendShip;
	}

	public UnconfirmedFriend removeUnconfirmedFriendShip(UnconfirmedFriend unconfirmedFriendShip) {
		getUnconfirmedFriendShips().remove(unconfirmedFriendShip);
		unconfirmedFriendShip.setUser(null);

		return unconfirmedFriendShip;
	}

	public List<Visitor> getConfirmedVisitors() {
		return this.confirmedVisitors;
	}
	
	public List<UnconfirmedFriend> getRequestedFriendShips() {
		return this.requestedFriendShips;
	}


	public void setConfirmedVisitors(List<Visitor> visitors) {
		this.confirmedVisitors = visitors;
	}

	public Visitor addConfirmedVisitor(Visitor visitor) {
		getConfirmedVisitors().add(visitor);
		visitor.setUser(this);

		return visitor;
	}

	public Visitor removeVisitor(Visitor visitor) {
		getConfirmedVisitors().remove(visitor);
		visitor.setUser(null);

		return visitor;
	}
	
    private List<User> getFriends(List<?  extends Friend> friendships){
        List<User> friends = new ArrayList<User>();

        for(Friend friendship : friendships) {
            friends.add(friendship.getFriend());
        }

        return friends;
    }
    
    private List<User> getFriendsRequest(List<?  extends Friend> friendships){
        List<User> friends = new ArrayList<User>();

        for(Friend friendship : friendships) {
            friends.add(friendship.getUser());
        }

        return friends;
    }
    
    public List<User> getFriends() {
    	return getFriends(confirmedFriendShips);
    }
    public List<User> getFriendInvites() {
    	return getFriends(unconfirmedFriendShips);
    }
    public List<User> getFriendRequests() {
    	return getFriendsRequest(requestedFriendShips);
    }

    
    private List<Appointment> getAppointments(List<?  extends Visitor> visitors){
        List<Appointment> appointments = new ArrayList<Appointment>();

        for(Visitor appointment : visitors) {
            appointments.add(appointment.getAppointment());
        }

        return appointments;
    }    
    
    public List<Appointment> getVisitingAppointments() {
    	return getAppointments(confirmedVisitors);
    }

    public List<Appointment> getInvitedAppointments() {
    	return getAppointments(invitedVisitors);
    }
    
    public SimpleUser toSimpleUser() {
    	SimpleUser user = new SimpleUser();
    	user.setId(id);
    	user.setUsername(username);
    	user.setLattitude(lattitude);
    	user.setLongitude(longitude);
    	user.setLatestLocationUpdate(latestLocationUpdate);
    	
    	Set<Integer> friends = new HashSet<Integer>();
    	for(User u : this.getFriends()) {
    		friends.add(u.getId());
    	}
    	user.setFriends(friends);
 
    	Set<Integer> apps = new HashSet<Integer>();
    	for(Appointment a : this.getVisitingAppointments()) {
    		apps.add(a.getId());
    	}
    	user.setVisitingAppointments(apps);
    	
    	Set<Integer> hApps = new HashSet<Integer>();
    	for(Appointment a : this.getHostedAppointments()) {
    		hApps.add(a.getId());
    	}
    	user.setVisitingAppointments(hApps);    	
    	
		return user;
    	
    }
    
}
