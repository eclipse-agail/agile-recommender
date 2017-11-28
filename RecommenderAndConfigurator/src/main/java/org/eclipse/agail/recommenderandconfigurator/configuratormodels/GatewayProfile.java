/*********************************************************************
 * Copyright (C) 2017 TUGraz.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/
package org.eclipse.agail.recommenderandconfigurator.configuratormodels;

public class GatewayProfile {
	
	private int[] supportedDataEncodingProtocolsOfGateway;
	private int[] supportedConnectivityProtocolsOfGateway;
	int userRequirementWeight_Performance;
	int userRequirementWeight_Reliability;
	int userRequirementWeight_Cost;
	private App[] installedApps;
	
	private String errorMessage;
	
	public int[] getSupportedDataEncodingProtocolsOfGateway() {
		return supportedDataEncodingProtocolsOfGateway;
	}
	public void setSupportedDataEncodingProtocolsOfGateway(int[] supportedDataEncodingProtocols) {
		this.supportedDataEncodingProtocolsOfGateway = supportedDataEncodingProtocols;
	}
	public int[] getSupportedConnectivityProtocolsOfGateway() {
		return supportedConnectivityProtocolsOfGateway;
	}
	public void setSupportedConnectivityProtocolsOfGateway(int[] supportedConnectivityProtocols) {
		this.supportedConnectivityProtocolsOfGateway = supportedConnectivityProtocols;
	}
	public int getUserRequirementWeight_Performance() {
		return userRequirementWeight_Performance;
	}
	public void setUserRequirementWeight_Performance(int userRequirementWeight_Performance) {
		this.userRequirementWeight_Performance = userRequirementWeight_Performance;
	}
	public int getUserRequirementWeight_Reliability() {
		return userRequirementWeight_Reliability;
	}
	public void setUserRequirementWeight_Reliability(int userRequirementWeight_Reliability) {
		this.userRequirementWeight_Reliability = userRequirementWeight_Reliability;
	}
	public int getUserRequirementWeight_Cost() {
		return userRequirementWeight_Cost;
	}
	public void setUserRequirementWeight_Cost(int userRequirementWeight_Cost) {
		this.userRequirementWeight_Cost = userRequirementWeight_Cost;
	}

	public App[] getInstalledApps() {
		return installedApps;
	}
	public void setInstalledApps(App[] installedApps) {
		this.installedApps = installedApps;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
