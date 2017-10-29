package com.hlaing.springoracle.model;

import java.util.UUID;

public class Location {

	private String id;
	private long postalCode;
	private double latitude;
	private double logitude;
	
	public Location() {};

	public Location(String id, long postalCode, double latitude, double logitude) {
		super();
		this.id = id;
		this.postalCode = postalCode;
		this.latitude = latitude;
		this.logitude = logitude;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public Location setLatitude(double latitude) {
		this.latitude = latitude;
		return this;
	}

	public double getLogitude() {
		return logitude;
	}

	public Location setLogitude(double logitude) {
		this.logitude = logitude;
		return this;
	}

	public String getId() {
		return id;
	}

	public Location setId(String id) {
		this.id = id;
		return this;
	}

	public long getPostalCode() {
		return postalCode;
	}

	public Location setPostalCode(long postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", postalCode=" + postalCode + ", latitude=" + latitude + ", logitude=" + logitude
				+ "]";
	}
	
	public Location computeId() {
		final StringBuilder hashInput = new StringBuilder().append(postalCode).append(latitude).append(logitude);
		
		id = UUID.nameUUIDFromBytes(hashInput.toString().getBytes()).toString();
		
		return this;
	}

}
