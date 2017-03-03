package at.tugraz.ist.agile.recommendermodels;

import org.springframework.beans.factory.annotation.Autowired;

public class GatewayProfile {
	

	public long userID;
	public String devices;
	public String apps;
	public String wfs;
	public String resources;
	public String location;
	public String pricingPreferences;
	
	public GatewayProfile(){
		this.userID = 0;
		this.devices = "";
		this.apps = "";
		this.wfs = ""; 
		this.resources = "";
		this.location = "";
		this.pricingPreferences = "";
		
	}
	public GatewayProfile (long id, String str1, String str2, String str3, String str4, String loc,String pricing){
		this.userID = id;
		this.devices = str1;
		this.apps = str2;
		this.wfs = str3; 
		this.resources = str4;
		this.location = loc;
		this.pricingPreferences = pricing;
	}
	
	
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public String getDevices() {
		return devices;
	}
	public void setDevices(String devices) {
		this.devices = devices;
	}
	public String getApps() {
		return apps;
	}
	public void setApps(String apps) {
		this.apps = apps;
	}
	public String getWfs() {
		return wfs;
	}
	public void setWfs(String wfs) {
		this.wfs = wfs;
	}
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPricingPreferences() {
		return pricingPreferences;
	}
	public void setPricingPreferences(String pricingPreferences) {
		this.pricingPreferences = pricingPreferences;
	}
	
	

}
