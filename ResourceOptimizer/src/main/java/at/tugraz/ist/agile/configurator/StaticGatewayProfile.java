package at.tugraz.ist.agile.configurator;

import java.util.List;

import at.tugraz.ist.agile.configuratormodels.App;
import at.tugraz.ist.agile.recommendermodels.Device;
import at.tugraz.ist.agile.recommendermodels.RecommendedApps;
import at.tugraz.ist.agile.recommendermodels.RecommendedDevices;
import at.tugraz.ist.agile.recommendermodels.RecommendedWFs;
import at.tugraz.ist.agile.recommendermodels.Workflow;

public class StaticGatewayProfile {
	
	public static RecommendedDevices devices=null;
	public static RecommendedApps apps=null;
	public static RecommendedWFs wfs=null;
	public static String resources=null;
	public static String location=null;
	public static String pricingPreferences=null;
	
	public static int[] supportedDataEncodingProtocolsOfGateway=null;
	public static int[] supportedConnectivityProtocolsOfGateway=null;
	public static int userRequirementWeight_Performance=0;
	public static int userRequirementWeight_Reliability=0;
	public static int userRequirementWeight_Cost=0;
	
	public static at.tugraz.ist.agile.configuratormodels.App[] appConfigurations=null;
	
}
