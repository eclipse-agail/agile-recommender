package org.eclipse.agail.recommenderandconfigurator;

public class AgileWorkflowModel {

	V1 [] v1;
	V2 v2;
}

class V1{
	String type;
	String id;
}

class V2{
	String rev;
	V1 [] flows;
}

