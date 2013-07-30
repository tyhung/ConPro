package trafficinfo.gps.speed.road;

import java.io.Serializable;

import org.apache.commons.logging.Log;

import trafficinfo.gps.speed.commutil.LogUtil;
import trafficinfo.gps.speed.coord.Wgs84Coord;

public class Road implements Serializable {

	protected static Log log = LogUtil.getLog( Road.class );
	
	/* 存放於資料庫中的資料 */
	private Integer id;
	private String roadName;
	private String direction;
	private float len;
	private int s;	
	
	protected int speedLimit;
	protected Wgs84Coord sC = null;
	protected Wgs84Coord eC = null;
	protected byte kind;
	protected byte roadClass;
	protected byte stopType;
	
	public Road() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getLen() {
		return len;
	}

	public void setLen(float len) {
		this.len = len;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public int getSpeedlimit() {
		return this.speedLimit;
	}
	
	public void setSpeedlimit(int s) {
		this.speedLimit = s;
	}
	
	public void setScoord(Wgs84Coord sCoord) {
		this.sC = sCoord;
	}
	
	public void setEcoord(Wgs84Coord eCoord) {
		this.eC = eCoord;
	}
	
	public Wgs84Coord getScoord() {
		return this.sC;
	}

	public Wgs84Coord getEcoord() {
		return this.eC;
	}
	
	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
		
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ID: ").append(this.id).append(" LEN:").append(this.len).append(" KPH: ").append(this.speedLimit)
		  .append(" S: ").append(this.s).append(" SCOORD:").append(this.getScoord()).append(" ECOORD:").append(this.getEcoord());
		return sb.toString();
	}

	public byte getKind() {
		return kind;
	}

	public void setKind(byte kind) {
		this.kind = kind;
	}

	public byte getRoadClass() {
		return roadClass;
	}

	public void setRoadClass(byte roadClass) {
		this.roadClass = roadClass;
	}

	public byte getStopType() {
		return stopType;
	}

	public void setStopType(byte stopType) {
		this.stopType = stopType;
	}
}