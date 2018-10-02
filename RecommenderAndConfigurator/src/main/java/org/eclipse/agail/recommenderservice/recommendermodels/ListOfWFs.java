/*******************************************************************************
 * Copyright (C) 2017 TUGraz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TUGraz - initial API and implementation
 ******************************************************************************/

package org.eclipse.agail.recommenderservice.recommendermodels;

import java.util.ArrayList;
import java.util.List;

public class ListOfWFs {
	
	private List<Workflow> wfList = new ArrayList<Workflow>();

	public List<Workflow> getWfList() {
		return wfList;
	}

	public void setWfList(List<Workflow> wfList) {
		this.wfList = wfList;
	}


}
