package at.tugraz.ist.agileconfigurator.resourceoptimizer.models;

public class GatewayProfile {
	
	private int[] pluggedDevices;
	private App[] installedApps;
	private int[] supportedDataEncodingProtocols;
	private int[] supportedConnectivityProtocols;
	int userRequirementWeight_Performance;
	int userRequirementWeight_Reliability;
	int userRequirementWeight_Cost;
	
	
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
	public int[] getSupportedDataEncodingProtocols() {
		return supportedDataEncodingProtocols;
	}
	public void setSupportedDataEncodingProtocols(int[] supportedDataEncodingProtocols) {
		this.supportedDataEncodingProtocols = supportedDataEncodingProtocols;
	}
	public int[] getSupportedConnectivityProtocols() {
		return supportedConnectivityProtocols;
	}
	public void setSupportedConnectivityProtocols(int[] supportedConnectivityProtocols) {
		this.supportedConnectivityProtocols = supportedConnectivityProtocols;
	}
	public int isUserRequirementWeight_Performance() {
		return userRequirementWeight_Performance;
	}
	public void setUserRequirementWeight_Performance(int userRequirementWeight_Performance) {
		this.userRequirementWeight_Performance = userRequirementWeight_Performance;
	}
	public int isUserRequirementWeight_Reliability() {
		return userRequirementWeight_Reliability;
	}
	public void setUserRequirementWeight_Reliability(int userRequirementWeight_Reliability) {
		this.userRequirementWeight_Reliability = userRequirementWeight_Reliability;
	}
	public int isUserRequirementWeight_Cost() {
		return userRequirementWeight_Cost;
	}
	public void setUserRequirementWeight_Cost(int userRequirementWeight_Cost) {
		this.userRequirementWeight_Cost = userRequirementWeight_Cost;
	}

	
	
}
