package com.test;

public class LogEvent {
	String id;
	String state;
	String type;
	String host;
	long timestamp;
	
	int duration ;
	boolean alert = false;
	
	public LogEvent(String id, String state, String type, String host, long timestamp, int duration, boolean alert) {
		super();
		this.id = id;
		this.state = state;
		this.type = type;
		this.host = host;
		this.timestamp = timestamp;
		this.duration = duration;
		this.alert = alert;
	}
	public LogEvent() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
