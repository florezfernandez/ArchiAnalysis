package co.edu.uniandes.archimate.analysis.entities;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.IRelationship;

public class RelationProperties {

	private IRelationship relationship;
	
	private List<PropertyDescription> properties;

	public IRelationship getElement() {
		return relationship;
	}

	public void setElement(IRelationship relationship) {
		this.relationship = relationship;
	}

	public List<PropertyDescription> getProperties() {
		return properties;
	}

	public RelationProperties() {
		properties = new ArrayList<PropertyDescription>();
	} 
	
}
