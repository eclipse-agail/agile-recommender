package at.tugraz.ist.agile.recommendermodels;

public class Cloud {

	public String title;
	public String link;
	public String accesstype;
	public String locations;
	public String middlewares;
	public String frameworks;
	public String runtimes;
	public String services;
	public String pricing;
	
	public Cloud(){
		
	}
	
	public Cloud(String title, String link, String accesstype, String locations, String middlewares, String frameworks,
			String runtimes, String services,String pricing) {
		super();
		this.title = title;
		this.link = link;
		this.accesstype = accesstype;
		this.locations = locations;
		this.middlewares = middlewares;
		this.frameworks = frameworks;
		this.runtimes = runtimes;
		this.services = services;
		this.pricing = pricing;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getAccesstype() {
		return accesstype;
	}


	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
	}


	public String getLocations() {
		return locations;
	}


	public void setLocations(String locations) {
		this.locations = locations;
	}


	public String getMiddlewares() {
		return middlewares;
	}


	public void setMiddlewares(String middlewares) {
		this.middlewares = middlewares;
	}


	public String getFrameworks() {
		return frameworks;
	}


	public void setFrameworks(String frameworks) {
		this.frameworks = frameworks;
	}


	public String getRuntimes() {
		return runtimes;
	}


	public void setRuntimes(String runtimes) {
		this.runtimes = runtimes;
	}


	public String getServices() {
		return services;
	}


	public void setServices(String services) {
		this.services = services;
	}


	public String getPricing() {
		return pricing;
	}


	public void setPricing(String pricing) {
		this.pricing = pricing;
	}
	
}
