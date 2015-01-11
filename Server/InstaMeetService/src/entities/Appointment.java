package entities;

import java.io.Serializable;

import javax.persistence.*;

import simpleEntities.SimpleAppointment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the Appointments database table.
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
	
	public Appointment(SimpleAppointment app) {
		this.id = app.getId();
		this.title = app.getTitle();
		this.description = app.getDescription();
		
		this.lattitude = app.getLattitude();
		this.longitude = app.getLongitude();
		
		this.startingTime = app.getStartingTime();
	}
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String description;

	private double lattitude;

	private double longitude;

	@Column(name="private")
	private String private_;

	@Column(name="starting_time")
	private Timestamp startingTime;

	private String title;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="hostId")
	private User hoster;

	//bi-directional many-to-one association to Visitor
	@OneToMany(mappedBy="appointment")
	private List<Visitor> confirmedVisitors;
	
	@OneToMany(mappedBy="appointment")
	private List<Visitor> invitedVisitors;

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

	public String getPrivate_() {
		return this.private_;
	}

	public void setPrivate_(String private_) {
		this.private_ = private_;
	}

	public Timestamp getStartingTime() {
		return this.startingTime;
	}

	public void setStartingTime(Timestamp startingTime) {
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

	public List<Visitor> getConfirmedVisitors() {
		return this.confirmedVisitors;
	}

	public void setConfirmedVisitors(List<Visitor> confirmedVisitors) {
		this.confirmedVisitors = confirmedVisitors;
	}

	public Visitor addConfirmedVisitor(Visitor confirmedVisitor) {
		getConfirmedVisitors().add(confirmedVisitor);
		confirmedVisitor.setAppointment(this);

		return confirmedVisitor;
	}

	public Visitor removeConfirmedVisitor(Visitor confirmedVisitor) {
		getConfirmedVisitors().remove(confirmedVisitor);
		confirmedVisitor.setAppointment(null);

		return confirmedVisitor;
	}

	public List<Visitor> getInvitedVisitors() {
		return this.invitedVisitors;
	}

	public void setInvitedVisitors(List<Visitor> invitedVisitors) {
		this.invitedVisitors = invitedVisitors;
	}

	public Visitor addInvitedVisitor(Visitor invitedVisitor) {
		getInvitedVisitors().add(invitedVisitor);
		invitedVisitor.setAppointment(this);

		return invitedVisitor;
	}

	public Visitor removeInvitedVisitor(Visitor invitedVisitor) {
		getInvitedVisitors().remove(invitedVisitor);
		invitedVisitor.setAppointment(null);

		return invitedVisitor;
	}
	
    private List<User> getVisitors(List<?  extends Visitor> visitors){
        List<User> visitingUsers = new ArrayList<User>();

        for(Visitor visitingUser : visitors) {
            visitingUsers.add(visitingUser.getUser());
        }

        return visitingUsers;
    }
    
	public List<User> getVisitingUsers() {
		return getVisitors(confirmedVisitors);
	}

	public List<User> getInvitedUsers() {
		return getVisitors(invitedVisitors);
	}
	
	
	public SimpleAppointment toSimpleAppointment() {
		SimpleAppointment app = new SimpleAppointment();
		app.setId(id);
		app.setTitle(title);
		app.setDescription(description);
		app.setLattitude(lattitude);
		app.setLongitude(longitude);
		app.setStartingTime(startingTime);
		
		app.setHoster(hoster.getId());
		
		Set<Integer> visitingUsers = new HashSet<Integer>();	
		for(User u : this.getVisitingUsers()) {
			visitingUsers.add(u.getId());
		}
		app.setVisitingUsers(visitingUsers);

		return app;
	}
}