package simpleEntities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


public class SimpleUser implements Serializable {

	private static final long serialVersionUID = -3089419899284441764L;
	
	public SimpleUser() {
		
	}


	private int id;

	private String username;


	private Timestamp latestLocationUpdate;
	private double lattitude;
	private double longitude;

	Set<Integer> friends;

	Set<Integer> hostedAppointments;	
	Set<Integer> visitingAppointments;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getLatestLocationUpdate() {
		return latestLocationUpdate;
	}
	public void setLatestLocationUpdate(Timestamp latestLocationUpdate) {
		this.latestLocationUpdate = latestLocationUpdate;
	}
	public double getLattitude() {
		return lattitude;
	}
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Set<Integer> getFriends() {
		return friends;
	}
	public void setFriends(Set<Integer> friends) {
		this.friends = friends;
	}
	public Set<Integer> getHostedAppointments() {
		return hostedAppointments;
	}
	public void setHostedAppointments(Set<Integer> hostedAppointments) {
		this.hostedAppointments = hostedAppointments;
	}
	public Set<Integer> getVisitingAppointments() {
		return visitingAppointments;
	}
	public void setVisitingAppointments(Set<Integer> visitingAppointments) {
		this.visitingAppointments = visitingAppointments;
	}
	
}
