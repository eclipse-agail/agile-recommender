package at.tugraz.ist.configurator.ChocoExtensions;

public class IntVar_Extension {
	
	private int ID;
	private int type;
	// type=0
	private int lowerBound;
	private int upperBound;
	// type=1
	private int[] domainArray;
	// type=2
	private int value;
	
	
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLowerBound() {
		return lowerBound;
	}
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}
	public int getUpperBound() {
		return upperBound;
	}
	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}
	public int[] getDomainArray() {
		return domainArray;
	}
	public void setDomainArray(int[] domainArray) {
		this.domainArray = domainArray;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	

}
