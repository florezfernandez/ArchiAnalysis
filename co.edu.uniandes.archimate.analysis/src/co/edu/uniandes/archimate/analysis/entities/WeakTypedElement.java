package co.edu.uniandes.archimate.analysis.entities;

import com.archimatetool.model.IArchimateElement;

public class WeakTypedElement {

	private Class<? extends IArchimateElement> clazz;
	
	private AnalysisProperty[] properties;

	public Class<? extends IArchimateElement> getClazz() {
		return clazz;
	}

	public void setClazz(Class<? extends IArchimateElement> relation) {
		this.clazz = relation;
	}

	public AnalysisProperty[]  getProperties() {
		return properties;
	}

	public void setProperties(AnalysisProperty[] properties) {
		this.properties = properties;
	}

	public WeakTypedElement(Class<? extends IArchimateElement> clazz,
			AnalysisProperty... properties) {
		super();
		this.clazz = clazz;
		this.properties = properties;
	}
	
	
	
}
