package co.edu.uniandes.archimate.analysis.entities;

import com.archimatetool.model.IRelationship;

public class WeakTypedRelation {

	private Class<? extends IRelationship> clazz;
	
	private AnalysisProperty[] properties;

	public Class<? extends IRelationship> getClazz() {
		return clazz;
	}

	public void setClazz(Class<? extends IRelationship> relation) {
		this.clazz = relation;
	}

	public AnalysisProperty[]  getProperties() {
		return properties;
	}

	public void setProperties(AnalysisProperty[] properties) {
		this.properties = properties;
	}

	public WeakTypedRelation(Class<? extends IRelationship> clazz,
			AnalysisProperty... properties) {
		super();
		this.clazz = clazz;
		this.properties = properties;
	}
	
	
	
}
