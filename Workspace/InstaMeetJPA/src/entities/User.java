package entities;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


/**
 * The persistent class for the user database table.
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
	@MapKey(name="id")
	private Map<Integer, Appointment> hostedAppointments;

	//bi-directional many-to-one association to Chatmessage
	@OneToMany(mappedBy="sender")
	@MapKeyJoinColumn(name="receiverId", table="chatmessages")
	private Map<Integer, Chatmessage> outChatmessages;

	//bi-directional many-to-one association to Chatmessage
	@OneToMany(mappedBy="receiver")
	@MapKeyJoinColumn(name="senderId", table="chatmessages")
	private Map<Integer, Chatmessage> inChatmessages;

	//bi-directional many-to-many association to Appointment
	@ManyToMany
	@JoinTable(
		name="appointments_has_user"
		, joinColumns={
			@JoinColumn(name="User_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Appointments_id")
			}
		)
	@MapKeyJoinColumn(name="Appointments_id", table="appointments_has_user")
	private Map<Integer, Appointment> visitingAppointments;

	//uni-directional many-to-many association to User
	@ManyToMany
	@JoinTable(
		name="friends"
		, joinColumns={
			@JoinColumn(name="User_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="friend_id")
			}
		)
	@MapKeyColumn(name="friend_id", table="friends")
	private java.util.Map<Integer, User> friends;

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

	public Map<Integer, Appointment> getHostedAppointments() {
		if(this.hostedAppointments == null)
			this.hostedAppointments = new HashMap<Integer, Appointment>();
		return this.hostedAppointments;
	}

	public void setHostedAppointments(Map<Integer, Appointment> hostedAppointments) {
		this.hostedAppointments = hostedAppointments;
	}

	public Appointment addHostedAppointment(Appointment hostedAppointment) {
		getHostedAppointments().put(hostedAppointment.getId(), hostedAppointment);
		hostedAppointment.setHoster(this);

		return hostedAppointment;
	}

	public Appointment removeHostedAppointment(Appointment hostedAppointment) {
		getHostedAppointments().remove(hostedAppointment);
		hostedAppointment.setHoster(null);

		return hostedAppointment;
	}

	public Map<Integer, Chatmessage> getOutChatmessages() {
		if(this.outChatmessages == null)
			this.outChatmessages = new HashMap<Integer, Chatmessage>();
		return this.outChatmessages;
	}

	public void setOutChatmessages(Map<Integer, Chatmessage> outChatmessages) {
		this.outChatmessages = outChatmessages;
	}

	public Chatmessage addOutChatmessage(Chatmessage outChatmessage) {
		getOutChatmessages().put(outChatmessage.getId(), outChatmessage);
		outChatmessage.setSender(this);

		return outChatmessage;
	}

	public Chatmessage removeOutChatmessage(Chatmessage outChatmessage) {
		getOutChatmessages().remove(outChatmessage);
		outChatmessage.setSender(null);

		return outChatmessage;
	}

	public Map<Integer, Chatmessage> getInChatmessages() {
		if(this.inChatmessages == null)
			this.inChatmessages = new HashMap<Integer, Chatmessage>();
		return this.inChatmessages;
	}

	public void setInChatmessages(Map<Integer, Chatmessage> inChatmessages) {
		this.inChatmessages = inChatmessages;
	}

	public Chatmessage addInChatmessage(Chatmessage inChatmessage) {
		getInChatmessages().put(inChatmessage.getId(), inChatmessage);
		inChatmessage.setReceiver(this);

		return inChatmessage;
	}

	public Chatmessage removeInChatmessage(Chatmessage inChatmessage) {
		getInChatmessages().remove(inChatmessage);
		inChatmessage.setReceiver(null);

		return inChatmessage;
	}

	public Map<Integer, Appointment> getVisitingAppointments() {
		if(this.visitingAppointments == null)
			this.visitingAppointments = new HashMap<Integer, Appointment>();
		return this.visitingAppointments;
	}

	public void setVisitingAppointments(Map<Integer, Appointment> visitingAppointments) {
		this.visitingAppointments = visitingAppointments;
	}

	public Map<Integer, User> getFriends() {
		if(this.friends == null)
			this.friends = new HashMap<Integer, User>();
		return this.friends;
	}

	public void setFriends(Map<Integer, User> friends) {
		this.friends = friends;
	}

}