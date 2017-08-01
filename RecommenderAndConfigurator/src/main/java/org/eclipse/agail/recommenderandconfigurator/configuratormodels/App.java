package org.eclipse.agail.recommenderandconfigurator.configuratormodels;

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
