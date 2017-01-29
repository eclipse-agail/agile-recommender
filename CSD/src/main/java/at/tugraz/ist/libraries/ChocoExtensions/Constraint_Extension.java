package at.tugraz.ist.libraries.ChocoExtensions;

public class Constraint_Extension {
	
	private int ID;
	private int type;
	
	// type=0 x = value
	private int var_1_ID;
	private int value_1;
	
	// type=1 if then
	private int var_2_ID;
	private int value_2;
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getVar_1_ID() {
		return var_1_ID;
	}
	public void setVar_1_ID(int var_1_ID) {
		this.var_1_ID = var_1_ID;
	}
	public int getValue_1() {
		return value_1;
	}
	public void setValue_1(int value_1) {
		this.value_1 = value_1;
	}
	public int getVar_2_ID() {
		return var_2_ID;
	}
	public void setVar_2_ID(int var_2_ID) {
		this.var_2_ID = var_2_ID;
	}
	public int getValue_2() {
		return value_2;
	}
	public void setValue_2(int value_2) {
		this.value_2 = value_2;
	}
	

}
