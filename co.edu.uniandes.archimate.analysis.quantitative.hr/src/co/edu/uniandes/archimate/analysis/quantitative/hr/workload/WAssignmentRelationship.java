package co.edu.uniandes.archimate.analysis.quantitative.hr.workload;

import com.archimatetool.model.impl.AssignmentRelationship;

public class WAssignmentRelationship {

	private AssignmentRelationship element;
	private Double i; //% of involvement of the role in the subprocess
	private Double c; //% of contribution of the role in the subprocess (a subprocess must be full filed by one or many roles and each one plays a specific function)
	private WBusinessRole wBusinessRole;
	
	public AssignmentRelationship getElement() {
		return element;
	}

	public Double getI() {
		return i;
	}

	public void setI(Double i) {
		this.i = i;
	}

	public Double getC() {
		return c;
	}

	public void setC(Double c) {
		this.c = c;
	}
	
	public WBusinessRole getWBusinessRole() {
		return wBusinessRole;
	}

	public void setWBusinessRole(WBusinessRole wBusinessRole) {
		this.wBusinessRole = wBusinessRole;
	}

	public WAssignmentRelationship(AssignmentRelationship element) {
		this.element = element;
	}

}
