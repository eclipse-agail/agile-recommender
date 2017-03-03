package at.tugraz.ist.agile.recommendermodels;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendedWFs {
	
	private List<Workflow> wfList;

	public List<Workflow> getWfList() {
		return wfList;
	}

	public void setWfList(List<Workflow> wfList) {
		this.wfList = wfList;
	}


}
