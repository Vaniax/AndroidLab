package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the Visitors database table.
 * 
 */
@Embeddable
public class VisitorPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="appointments_id", insertable=false, updatable=false)
	private int appointmentsId;

	@Column(name="user_id", insertable=false, updatable=false)
	private int userId;

	public VisitorPK() {
	}
	public int getAppointmentsId() {
		return this.appointmentsId;
	}
	public void setAppointmentsId(int appointmentsId) {
		this.appointmentsId = appointmentsId;
	}
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VisitorPK)) {
			return false;
		}
		VisitorPK castOther = (VisitorPK)other;
		return 
			(this.appointmentsId == castOther.appointmentsId)
			&& (this.userId == castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appointmentsId;
		hash = hash * prime + this.userId;
		
		return hash;
	}
}