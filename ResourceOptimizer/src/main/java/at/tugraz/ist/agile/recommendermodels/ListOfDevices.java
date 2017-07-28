/*******************************************************************************
 * Copyright (C) 2017 TU Graz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TU Graz - initial API and implementation
 ******************************************************************************/
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
