package org.eclipse.agail.recommenderandconfigurator;

public class AgileWorkflowModel {

	V1 [] v1;
	V2 v2;
}

class V1{
	String type;
	String id;
	String label;
}

class V2{
	String rev;
	V1 [] flows;
}

