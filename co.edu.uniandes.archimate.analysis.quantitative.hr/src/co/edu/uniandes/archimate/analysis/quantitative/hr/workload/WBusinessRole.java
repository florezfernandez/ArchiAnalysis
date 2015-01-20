package co.edu.uniandes.archimate.analysis.quantitative.hr.workload;

import com.archimatetool.model.impl.BusinessRole;

public class WBusinessRole {

	private BusinessRole element;
	private Double n; // number of employees (human resources) assigned to this role

	public BusinessRole getElement() {
		return element;
	}

	public Double getN() {
		return n;
	}

	public void setN(Double n) {
		this.n = n;
	}

	public WBusinessRole(BusinessRole element) {
		this.element = element;
	}

}
