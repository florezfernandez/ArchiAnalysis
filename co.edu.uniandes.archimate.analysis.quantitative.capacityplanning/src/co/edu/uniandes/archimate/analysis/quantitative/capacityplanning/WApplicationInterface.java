package co.edu.uniandes.archimate.analysis.quantitative.capacityplanning;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.impl.ApplicationInterface;

public class WApplicationInterface extends WAbstractSDTElement{

	private ApplicationInterface element;
	private List<WUsedByRelationship> wUsedByRelationships;	
	private Double txSize;
	private Double maxTt;

	public ApplicationInterface getElement() {
		return element;
	}

	public List<WUsedByRelationship> getWUsedByRelationships() {
		return wUsedByRelationships;
	}
	public void addWUsedByRelationships(WUsedByRelationship wUsedByRelationship) {
		this.wUsedByRelationships.add(wUsedByRelationship);
	}

	public Double getTxSize() {
		return txSize;
	}
	public void setTxSize(Double txSize) {
		this.txSize = txSize;
	}

	public Double getMaxTt() {
		return maxTt;
	}
	public void setMaxTt(Double maxTt) {
		this.maxTt = maxTt;
	}

	public WApplicationInterface(ApplicationInterface element) {
		this.element = element;
		this.wUsedByRelationships = new ArrayList<WUsedByRelationship>();
	}


}
