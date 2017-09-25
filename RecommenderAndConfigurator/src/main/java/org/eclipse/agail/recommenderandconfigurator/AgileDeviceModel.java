package org.eclipse.agail.recommenderandconfigurator;

public class AgileDeviceModel {

	AgileDevice[] devices;
	
}

class AgileDevice{
	
	String deviceId;
	String address;
	String name	;
	String description;
	String protocol	;
	String path	;
	Stream [] streams;
}

class Stream{
	String id;
	String unit;
}