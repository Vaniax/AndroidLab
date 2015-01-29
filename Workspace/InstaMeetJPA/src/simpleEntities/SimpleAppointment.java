package simpleEntities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


public class SimpleAppointment implements Serializable {

	private static final long serialVersionUID = -8489951569455977180L;
	
	public SimpleAppointment() {
		// TODO Auto-generated constructor stub
	}
	


	private int id;
	private String title;
	private String description;

	private double lattitude;
	private double longitude;

	private long startingTime;
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

	public long getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(long startingTime) {
		this.startingTime = startingTime;
	}

	public Integer getHoster() {
		return hoster;
	}

	public void setHoster(Integer hoster) {
		this.hoster = hoster;
	}

	public Set<Integer> getVisitingUsers() {
		if(visitingUsers == null) {
			visitingUsers = new HashSet<Integer>();
		}
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
