package at.tugraz.ist.agileconfigurator.resourceoptimizer.models;

public class GatewayProfile {
	
	private int[] pluggedDevices;
	private App[] installedApps;
	private Enum_DataEncodingProtocols[] supportedDataEncodingProtocols;
	private Enum_ConnectivityProtocols[] supportedConnectivityProtocols;
	boolean userRequirementWeight_Performance;
	boolean userRequirementWeight_Reliability;
	boolean userRequirementWeight_Cost;
	
	
	public int[] getPluggedDevices() {
		return pluggedDevices;
	}
	public void setPluggedDevices(int[] pluggedDevices) {
		this.pluggedDevices = pluggedDevices;
	}
	public App[] getInstalledApps() {
		return installedApps;
	}
	public void setInstalledApps(App[] installedApps) {
		this.installedApps = installedApps;
	}
	public Enum_DataEncodingProtocols[] getSupportedDataEncodingProtocols() {
		return supportedDataEncodingProtocols;
	}
	public void setSupportedDataEncodingProtocols(Enum_DataEncodingProtocols[] supportedDataEncodingProtocols) {
		this.supportedDataEncodingProtocols = supportedDataEncodingProtocols;
	}
	public Enum_ConnectivityProtocols[] getSupportedConnectivityProtocols() {
		return supportedConnectivityProtocols;
	}
	public void setSupportedConnectivityProtocols(Enum_ConnectivityProtocols[] supportedConnectivityProtocols) {
		this.supportedConnectivityProtocols = supportedConnectivityProtocols;
	}
	public boolean isUserRequirementWeight_Performance() {
		return userRequirementWeight_Performance;
	}
	public void setUserRequirementWeight_Performance(boolean userRequirementWeight_Performance) {
		this.userRequirementWeight_Performance = userRequirementWeight_Performance;
	}
	public boolean isUserRequirementWeight_Reliability() {
		return userRequirementWeight_Reliability;
	}
	public void setUserRequirementWeight_Reliability(boolean userRequirementWeight_Reliability) {
		this.userRequirementWeight_Reliability = userRequirementWeight_Reliability;
	}
	public boolean isUserRequirementWeight_Cost() {
		return userRequirementWeight_Cost;
	}
	public void setUserRequirementWeight_Cost(boolean userRequirementWeight_Cost) {
		this.userRequirementWeight_Cost = userRequirementWeight_Cost;
	}

	
	
}
