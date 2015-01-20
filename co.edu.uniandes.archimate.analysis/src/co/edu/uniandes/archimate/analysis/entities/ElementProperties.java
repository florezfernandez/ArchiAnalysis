package co.edu.uniandes.archimate.analysis.entities;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.IArchimateElement;

public class ElementProperties {

	private IArchimateElement element;
	
	private List<PropertyDescription> properties;

	public IArchimateElement getElement() {
		return element;
	}

	public void setElement(IArchimateElement element) {
		this.element = element;
	}

	public List<PropertyDescription> getProperties() {
		return properties;
	}
	
	public ElementProperties() {
		properties = new ArrayList<PropertyDescription>();
	} 
}
