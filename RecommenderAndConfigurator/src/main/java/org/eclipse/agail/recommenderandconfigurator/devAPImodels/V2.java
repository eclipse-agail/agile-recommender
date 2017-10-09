package org.eclipse.agail.recommenderandconfigurator.devAPImodels;

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