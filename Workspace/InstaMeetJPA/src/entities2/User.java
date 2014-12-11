package entities2;

import java.io.Serializable;

import javax.persistence.*;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


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
	private List<Appointment> hostedAppointments;

	//bi-directional many-to-one association to Chatmessage
	@OneToMany(mappedBy="sender")
	private List<Chatmessage> outChatmessages;

	//bi-directional many-to-one association to Chatmessage
	@OneToMany(mappedBy="receiver")
	private List<Chatmessage> inChatmessages;

	//bi-directional many-to-many association to Appointment
	@ManyToMany
	@JoinTable(
		name="Visitors"
		, joinColumns={
			@JoinColumn(name="User_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Appointments_id")
			}
		)
	private List<Appointment> visitingAppointments;

	//uni-directional many-to-many association to User
	@ManyToMany
	@JoinTable(
		name="Friends"
		, joinColumns={
			@JoinColumn(name="User_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="friend_id")
			}
		)
	List<User> friends;

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
		if(this.hostedAppointments == null)
			this.hostedAppointments = new ArrayList<Appointment>();
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

	public List<Chatmessage> getOutChatmessages() {
		if(this.outChatmessages == null)
			this.outChatmessages = new ArrayList<Chatmessage>();
		return this.outChatmessages;
	}

	public void setOutChatmessages(List<Chatmessage> outChatmessages) {
		this.outChatmessages = outChatmessages;
	}

	public Chatmessage addOutChatmessage(Chatmessage outChatmessage) {
		getOutChatmessages().add(outChatmessage);
		outChatmessage.setSender(this);

		return outChatmessage;
	}

	public Chatmessage removeOutChatmessage(Chatmessage outChatmessage) {
		getOutChatmessages().remove(outChatmessage);
		outChatmessage.setSender(null);

		return outChatmessage;
	}

	public List<Chatmessage> getInChatmessages() {
		if(this.inChatmessages == null)
			this.inChatmessages = new ArrayList<Chatmessage>();
		return this.inChatmessages;
	}

	public void setInChatmessages(List<Chatmessage> inChatmessages) {
		this.inChatmessages = inChatmessages;
	}

	public Chatmessage addInChatmessage(Chatmessage inChatmessage) {
		getInChatmessages().add(inChatmessage);
		inChatmessage.setReceiver(this);

		return inChatmessage;
	}

	public Chatmessage removeInChatmessage(Chatmessage inChatmessage) {
		getInChatmessages().remove(inChatmessage);
		inChatmessage.setReceiver(null);

		return inChatmessage;
	}

	public List<Appointment> getVisitingAppointments() {
		if(this.visitingAppointments == null)
			this.visitingAppointments = new ArrayList<Appointment>();
		return this.visitingAppointments;
	}

	public void setVisitingAppointments(List<Appointment> visitingAppointments) {
		this.visitingAppointments = visitingAppointments;
	}

	public List<User> getFriends() {
		if(this.friends == null)
			this.friends = new ArrayList<User>();
		return this.friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

}