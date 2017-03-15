package at.tugraz.ist.agile.configurator;

import java.util.List;

import at.tugraz.ist.agile.configuratormodels.App;
import at.tugraz.ist.agile.recommendermodels.Device;
import at.tugraz.ist.agile.recommendermodels.RecommendedApps;
import at.tugraz.ist.agile.recommendermodels.RecommendedDevices;
import at.tugraz.ist.agile.recommendermodels.RecommendedWFs;
import at.tugraz.ist.agile.recommendermodels.Workflow;

public class StaticServiceConfiguration {
	
	public static boolean recommenderServiceForDevelopmentUIActive;
	public static boolean recommenderServiceForDeviceManagementUIActive;
	public static boolean recommenderServiceForAppManagementUIActive;
	public static boolean allowRecommenderServerToUseGatewayProfile;
	
	public StaticServiceConfiguration(){
		
	}
}
