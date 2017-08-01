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

package org.eclipse.agail.recommenderandconfigurator.configuratormodels;

public class Protocol{
	
	private int performance;
	private int reliability;
	private int cost;
	
	
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getPerformance() {
		return performance;
	}
	public void setPerformance(int performance) {
		this.performance = performance;
	}
	public int getReliability() {
		return reliability;
	}
	public void setReliability(int reliability) {
		this.reliability = reliability;
	}
	
	

}
