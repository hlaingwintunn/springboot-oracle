package com.hlaing.springoracle.model;

public class Location {

	private double latitude;
	private double logitude;

	public double getLatitude() {
		return latitude;
	}

	public Location(double latitude, double logitude) {
		super();
		this.latitude = latitude;
		this.logitude = logitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLogitude() {
		return logitude;
	}

	public void setLogitude(double logitude) {
		this.logitude = logitude;
	}

	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", logitude=" + logitude + "]";
	}

}
