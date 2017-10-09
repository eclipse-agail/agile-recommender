package org.eclipse.agail.recommenderandconfigurator.devAPImodels;


public class AgileDevice{
	
	String deviceId;
	String address;
	String name	;
	String description;
	String protocol	;
	String path	;
	AgileStream [] streams;
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public AgileStream[] getStreams() {
		return streams;
	}
	public void setStreams(AgileStream[] streams) {
		this.streams = streams;
	}
}
