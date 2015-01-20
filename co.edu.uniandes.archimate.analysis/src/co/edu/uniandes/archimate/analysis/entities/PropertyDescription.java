package co.edu.uniandes.archimate.analysis.entities;

public class PropertyDescription {

	private String propertyKey;
	
	private Boolean isMandatory;
	
	private Class<? extends Object> clazz;

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	public Class<? extends Object> getType() {
		return clazz;
	}

	public void setType(Class<? extends Object> clazz) {
		this.clazz = clazz;
	}

	public PropertyDescription(String propertyKey, Boolean isMandatory,
			Class<Object> clazz) {
		super();
		this.propertyKey = propertyKey;
		this.isMandatory = isMandatory;
		this.clazz = clazz;
	}

	public PropertyDescription() {
		super();
		this.propertyKey = "";
		this.isMandatory = true;
		this.clazz = Object.class;
	}
	
	
	
}
