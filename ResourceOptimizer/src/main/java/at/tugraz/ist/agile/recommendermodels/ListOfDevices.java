package at.tugraz.ist.agile.recommendermodels;

import java.util.ArrayList;
import java.util.List;

public class ListOfDevices {
	
	private List<Device> deviceList = new ArrayList<Device>();

	public ListOfDevices(){
		
	}
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	public void addDevice(Device device) {
		this.deviceList.add(device);
	}

}
