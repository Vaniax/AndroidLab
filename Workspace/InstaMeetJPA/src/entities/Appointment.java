package entities;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the appointments database table.
 * 
 */
@Entity
@Table(name="appointments")
@NamedQueries({
	@NamedQuery(name="Appointment.findAll", query="SELECT a FROM Appointment a")
	//@NamedQuery(name="Appointment.visiting", query="SELECT a FROM appointments a, appointments_has_user b WHERE a.id = b.Appointments_id AND b.User_id = :userId")
})
public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String description;

	private double lattitude;

	private double longitude;

	@Column(name="starting_time")
	private Time startingTime;

	private String title;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="hostId")
	private User hoster;

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="visitingAppointments")
	private List<User> visitingUsers;

	public Appointment() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Time getStartingTime() {
		return this.startingTime;
	}

	public void setStartingTime(Time startingTime) {
		this.startingTime = startingTime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getHoster() {
		return this.hoster;
	}

	public void setHoster(User hoster) {
		this.hoster = hoster;
	}

	public List<User> getVisitingUsers() {
		if(this.visitingUsers == null)
			this.visitingUsers = new ArrayList<User>();
		return this.visitingUsers;
	}

	public void setVisitingUsers(List<User> visitingUsers) {
		this.visitingUsers = visitingUsers;
	}

}