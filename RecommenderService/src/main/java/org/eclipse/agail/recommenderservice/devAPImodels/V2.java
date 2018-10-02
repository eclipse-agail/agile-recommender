/*********************************************************************
 * Copyright (C) 2017 TUGraz.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/

package org.eclipse.agail.recommenderservice.devAPImodels;

public class V2{
	String rev;
	V1 [] flows;
	
	public V2(){}
	
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public V1[] getFlows() {
		return flows;
	}
	public void setFlows(V1[] flows) {
		this.flows = flows;
	}
}