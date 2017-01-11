package at.tugraz.ist.agileconfigurator.resourceoptimizer.models;

public class App {
	
	
	public App(boolean Test) {
		this.setName("deneme");
		this.setXorConnectivitiyProtocols(new int[]{1,2});
		this.setXorDataEncodingProtocols(new int[]{1,2});
	
	}
	
	public App() {
		super();
	}
	
	private String name;
	
	//private Enum_DataEncodingProtocols [] xorDataEncodingProtocols;
	private int [] xorDataEncodingProtocols;
	
	//private Enum_ConnectivityProtocols [] xorConnectivitiyProtocols;
	private int [] xorConnectivitiyProtocols;
	
	private String url;
	
	private Enum_DataEncodingProtocols inUse_DataEncodingProtocol;
	private Enum_ConnectivityProtocols inUse_ConnectivitiyProtocol;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getXorDataEncodingProtocols() {
		return xorDataEncodingProtocols;
	}
	public void setXorDataEncodingProtocols(int[] xorDataEncodingProtocols) {
		this.xorDataEncodingProtocols = xorDataEncodingProtocols;
	}
	public int[] getXorConnectivitiyProtocols() {
		return xorConnectivitiyProtocols;
	}
	public void setXorConnectivitiyProtocols(int[] xorConnectivitiyProtocols) {
		this.xorConnectivitiyProtocols = xorConnectivitiyProtocols;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Enum_DataEncodingProtocols getInUse_DataEncodingProtocol() {
		return inUse_DataEncodingProtocol;
	}
	public void setInUse_DataEncodingProtocol(Enum_DataEncodingProtocols inUse_DataEncodingProtocol) {
		this.inUse_DataEncodingProtocol = inUse_DataEncodingProtocol;
	}
	public Enum_ConnectivityProtocols getInUse_ConnectivitiyProtocol() {
		return inUse_ConnectivitiyProtocol;
	}
	public void setInUse_ConnectivitiyProtocol(Enum_ConnectivityProtocols inUse_ConnectivitiyProtocol) {
		this.inUse_ConnectivitiyProtocol = inUse_ConnectivitiyProtocol;
	}

}
