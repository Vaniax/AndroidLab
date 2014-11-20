package entities;

public class Location {
	private double longitude;
	private double lattitude;
	
	public Location() {
		longitude = 0;
		lattitude = 0;
	}
	
	public Location(double longitude, double lattitude) {
		this.longitude = longitude;
		this.lattitude = lattitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLattitude() {
		return lattitude;
	}
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}
}
