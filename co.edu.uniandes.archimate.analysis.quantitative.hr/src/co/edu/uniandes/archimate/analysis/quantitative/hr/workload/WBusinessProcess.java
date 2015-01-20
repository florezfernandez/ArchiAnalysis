package co.edu.uniandes.archimate.analysis.quantitative.hr.workload;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.impl.BusinessProcess;

public class WBusinessProcess {

	private BusinessProcess element;
	private Double f; //Daily arriving Instances
	private List<WBusinessSubprocess> wBusinessSubprocesses;
	
	public BusinessProcess getElement() {
		return element;
	}
	
	public Double getF() {
		return f;
	}

	public void setF(Double f) {
		this.f = f;
	}
	
	public void addWBusinessSubprocesses(WBusinessSubprocess wBusinessSubprocesses){
		this.wBusinessSubprocesses.add(wBusinessSubprocesses);
	}
	
	public List<WBusinessSubprocess> getWBusinessSubprocesses(){
		return this.wBusinessSubprocesses;
	}
	

	public WBusinessProcess(BusinessProcess element) {
		this.element = element;
		this.wBusinessSubprocesses = new ArrayList<WBusinessSubprocess>();
	}

}
