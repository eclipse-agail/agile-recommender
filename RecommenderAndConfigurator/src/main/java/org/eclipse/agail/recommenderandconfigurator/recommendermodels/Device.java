package org.eclipse.agail.recommenderandconfigurator.recommendermodels;

public class Device {
	
	private String title;
	//public String content;
	private String href;

	
	public Device(){
		
	}
	
	public Device(String title, String href){
		this.title = title;
		this.href = href;

	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
}
