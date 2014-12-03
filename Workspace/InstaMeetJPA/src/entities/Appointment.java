package entities;

import java.io.Serializable;

import javax.persistence.*;

import simpleEntities.simpleAppointment;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;


/**
 * The persistent class for the appointments database table.
 * 
 */
@Entity
@Table(name="Appointments")
@NamedQueries({
	@NamedQuery(name="Appointment.findAll", query="SELECT a FROM Appointment a"),
	@NamedQuery(name="Appointment.findId", query="SELECT a FROM Appointment a where a.id = :id"),
	@NamedQuery(name="Appointment.nearby", query="SELECT a, (6371 * FUNC('acos',FUNC('cos',FUNC('radians',:lat))*FUNC('cos',FUNC('radians',a.lattitude))*FUNC('cos',FUNC('radians',a.longitude)-FUNC('radians',:lon)) + FUNC('sin',FUNC('radians',:lat)) * FUNC('sin',FUNC('radians',a.lattitude)) )) AS distance FROM Appointment a HAVING distance < 10000 ORDER BY distance")

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
	
	public Appointment(simpleAppointment app) {
		this.id = app.getId();
		this.title = app.getTitle();
		this.description = app.getDescription();
		
		this.lattitude = app.getLattitude();
		this.longitude = app.getLongitude();
		
		this.startingTime = app.getStartingTime();
	}
	

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="hostId")
	private User hoster;

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="visitingAppointments")
	@MapKey(name="id")
	private Map<Integer, User> visitingUsers;

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

	public Map<Integer, User> getVisitingUsers() {
		if(this.visitingUsers == null)
			this.visitingUsers = new HashMap<Integer, User>();
		return this.visitingUsers;
	}

	public void setVisitingUsers(Map<Integer, User> visitingUsers) {
		this.visitingUsers = visitingUsers;
	}

}