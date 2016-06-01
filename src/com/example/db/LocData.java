package com.example.db;

public class LocData {
	private String alert;
	private String content;
	private String id;
	private double lon;
	private double lat;
	private String time;
	private String loc;

	public LocData(double lon, double lat, String time, String loc) {
		super();
		this.lon = lon;
		this.lat = lat;
		this.time = time;
		this.loc = loc;
	}

	public LocData(double lon, double lat, String time) {
		super();
		this.lon = lon;
		this.lat = lat;
		this.time = time;
	}

	public LocData(String alert, String content, String id) {
		super();
		this.alert = alert;
		this.content = content;
		this.id = id;
	}

	public LocData(String alert, String content, String id, double lon,
			double lat) {
		super();
		this.alert = alert;
		this.content = content;
		this.id = id;
		this.lon = lon;
		this.lat = lat;
	}

	@Override
	public String toString() {
		return "LocData [alert=" + alert + ", content=" + content + ", id="
				+ id + ", lon=" + lon + ", lat=" + lat + ", time=" + time + "]";
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

}
