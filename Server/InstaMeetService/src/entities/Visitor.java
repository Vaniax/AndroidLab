package entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Visitors database table.
 * 
 */
@Entity
@Table(name="Visitors")
@NamedQuery(name="Visitor.findAll", query="SELECT v FROM Visitor v")
@DiscriminatorColumn(name = "invitedOnly")
public class Visitor implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VisitorPK id;

	private boolean invitedOnly;

	//bi-directional many-to-one association to Appointment
	@ManyToOne
	@JoinColumn(name="appointments_id")
	private Appointment appointment;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public Visitor() {
	}

	public VisitorPK getId() {
		return this.id;
	}

	public void setId(VisitorPK id) {
		this.id = id;
	}

	public boolean getInvitedOnly() {
		return this.invitedOnly;
	}

	public void setInvitedOnly(boolean invitedOnly) {
		this.invitedOnly = invitedOnly;
	}

	public Appointment getAppointment() {
		return this.appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}