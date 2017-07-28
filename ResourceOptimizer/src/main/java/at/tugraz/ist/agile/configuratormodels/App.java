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
package at.tugraz.ist.agile.configuratormodels;

public class App {
	
	public App() {
		super();
	}
	
	private String name;
	private String url;
	private int inUse_DataEncodingProtocol;
	private int inUse_ConnectivitiyProtocol;
	private int [] supportedDataEncodingProtocolsOfApp;
	private int [] supportedConnectivitiyProtocolsOfApp;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getInUse_DataEncodingProtocol() {
		return inUse_DataEncodingProtocol;
	}
	public void setInUse_DataEncodingProtocol(int inUse_DataEncodingProtocol) {
		this.inUse_DataEncodingProtocol = inUse_DataEncodingProtocol;
	}
	public int getInUse_ConnectivitiyProtocol() {
		return inUse_ConnectivitiyProtocol;
	}
	public void setInUse_ConnectivitiyProtocol(int inUse_ConnectivitiyProtocol) {
		this.inUse_ConnectivitiyProtocol = inUse_ConnectivitiyProtocol;
	}
	
	public int[] getSupportedDataEncodingProtocolsOfApp() {
		return supportedDataEncodingProtocolsOfApp;
	}
	public void setSupportedDataEncodingProtocolsOfApp(int[] xorDataEncodingProtocols) {
		this.supportedDataEncodingProtocolsOfApp = xorDataEncodingProtocols;
	}
	public int[] getSupportedConnectivitiyProtocolsOfApp() {
		return supportedConnectivitiyProtocolsOfApp;
	}
	public void setSupportedConnectivitiyProtocolsOfApp(int[] xorConnectivitiyProtocols) {
		this.supportedConnectivitiyProtocolsOfApp = xorConnectivitiyProtocols;
	}
	
	

}
