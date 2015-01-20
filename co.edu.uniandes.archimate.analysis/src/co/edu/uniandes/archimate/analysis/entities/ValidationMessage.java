package co.edu.uniandes.archimate.analysis.entities;

import com.archimatetool.model.IArchimateElement;

public class ValidationMessage {

	private ValidationMessageType type; 
	private IArchimateElement element;  //TODO: Change IArchiMateElement by a more generic object (EMF) maybe EObjectImpl
	private String message;

	public ValidationMessageType getType() {
		return type;
	}
	public void setType(ValidationMessageType type) {
		this.type = type;
	}
	public IArchimateElement getElement() {
		return element;
	}
	public void setElement(IArchimateElement element) {
		this.element = element;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public ValidationMessage(ValidationMessageType type,
			IArchimateElement element, String message) {
		super();
		this.type = type;
		this.element = element;
		this.message = message;
	}

	public String toString(){
		return ((element!=null?element.getClass().getSimpleName() +" - " + element.getName()+": ":"") + message).replace(System.getProperty("line.separator"), " "); 
	}
}
