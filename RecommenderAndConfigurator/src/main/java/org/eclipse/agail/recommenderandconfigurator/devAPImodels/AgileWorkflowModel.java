/*********************************************************************
 * Copyright (C) 2017 TUGraz.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/
package org.eclipse.agail.recommenderandconfigurator.devAPImodels;

public class AgileWorkflowModel {

	V1 [] v1;
	V2 v2;
	
	public AgileWorkflowModel(){}
	
	public V1[] getV1() {
		return v1;
	}
	public void setV1(V1[] v1) {
		this.v1 = v1;
	}
	public V2 getV2() {
		return v2;
	}
	public void setV2(V2 v2) {
		this.v2 = v2;
	}

}


