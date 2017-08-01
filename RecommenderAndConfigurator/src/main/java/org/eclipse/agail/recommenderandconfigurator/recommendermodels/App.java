package org.eclipse.agail.recommenderandconfigurator.recommendermodels;

public class App {
	
	private String title;
	//public String content;
	private String href;
	private int stars = 0;
	private int downloads = 0;
	
	
	public App(){
		
	}
	
	public App(String title, String href, int stars, int downloads){
		this.title = title;
		this.href = href;
		this.stars = stars;
		this.downloads = downloads;
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

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getDownloads() {
		return downloads;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}
	
}
