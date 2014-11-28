package simpleEntities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Set;

import entities.Appointment;


public class simpleAppointment implements Serializable {

	private static final long serialVersionUID = -8489951569455977180L;
	
	public simpleAppointment(Appointment app) {
		this.id = app.getId();
		this.title = app.getTitle();
		this.description = app.getDescription();
		
		this.lattitude = app.getLattitude();
		this.longitude = app.getLongitude();
		
		this.startingTime = app.getStartingTime();
		this.hoster = app.getHoster().getId();
		this.visitingUsers = app.getVisitingUsers().keySet();
	}

	private int id;
	private String title;
	private String description;

	private double lattitude;
	private double longitude;

	private Time startingTime;
	private int hoster;
	
	private double distance;
	
	Set<Integer> visitingUsers;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Time getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Time startingTime) {
		this.startingTime = startingTime;
	}

	public Integer getHoster() {
		return hoster;
	}

	public void setHoster(Integer hoster) {
		this.hoster = hoster;
	}

	public Set<Integer> getVisitingUsers() {
		return visitingUsers;
	}

	public void setVisitingUsers(Set<Integer> visitingUsers) {
		this.visitingUsers = visitingUsers;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
