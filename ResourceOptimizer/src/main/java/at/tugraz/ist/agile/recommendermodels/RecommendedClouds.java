package at.tugraz.ist.agile.recommendermodels;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendedClouds {
	
	private List<Cloud> cloudList;

	public List<Cloud> getCloudList() {
		return cloudList;
	}

	public void setCloudList(List<Cloud> cloudList) {
		this.cloudList = cloudList;
	}

}
