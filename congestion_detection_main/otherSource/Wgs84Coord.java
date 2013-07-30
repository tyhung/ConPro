package trafficinfo.gps.speed.coord;

import java.io.Serializable;

/**
 * Wgs84版本的經緯度
 * @author ClayChen
 *
 */
public class Wgs84Coord implements Serializable{
	private double longitude;
	private double latitude;

	public Wgs84Coord() {		
	}
	
	public Wgs84Coord(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	@Override
	public boolean equals(Object b) {
		Wgs84Coord a = null;
		if (b instanceof Wgs84Coord) {
			a = (Wgs84Coord) b;
			if (this.longitude == a.longitude && this.latitude == a.latitude)
				return true;
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		return (int) (this.longitude + this.latitude);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(longitude).append(",").append(latitude).append(
				")");
		return sb.toString();
	}
}
