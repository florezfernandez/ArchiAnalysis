package co.edu.uniandes.archimate.analysis.quantitative.hr.workload;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.impl.BusinessProcess;

public class WBusinessSubprocess {

	private BusinessProcess element;
	private Double l; //% of arriving instances, after an split caused by a junction the % of instances arriving to each subprocess will be divided (e.g. 50% of the claims are payed and the other 50% are rejected)
	private Double t; //the average daily time spent per each instance expressed in minutes
	private List<WAssignmentRelationship> wAssignmentRelationships;

	public BusinessProcess getElement() {
		return element;
	}
	
	public Double getL() {
		return l;
	}

	public void setL(Double l) {
		this.l = l;
	}

	public Double getT() {
		return t;
	}

	public void setT(Double t) {
		this.t = t;
	}
	
	public void addWAssignmentRelationship(WAssignmentRelationship wAssignmentRelationship)
	{
		this.wAssignmentRelationships.add(wAssignmentRelationship);		
	}
	
	public List<WAssignmentRelationship> getWAssignmentRelationships()
	{
		return  this.wAssignmentRelationships;
	}
	
	public WBusinessSubprocess(BusinessProcess element) {
		this.element = element;
		this.wAssignmentRelationships = new ArrayList<WAssignmentRelationship>();
	}
	
}
