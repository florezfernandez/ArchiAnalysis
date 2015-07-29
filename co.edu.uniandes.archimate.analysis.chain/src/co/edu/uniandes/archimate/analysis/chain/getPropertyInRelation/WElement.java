package co.edu.uniandes.archimate.analysis.chain.getPropertyInRelation;


import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IProperty;

public class WElement {

	private IArchimateElement element;
	private String id;
	private ArrayList<String> idTarget;
	private Class<?> clase;



	private String name;
	
	
	public Object getElement() {
		return element;
	}
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}

	public WElement(EObject eObject) {
		setClase(eObject.getClass());
		this.element = (IArchimateElement) eObject;
		setId(element.getId());
		name=element.getName();
		idTarget=new ArrayList<String>();
	}

	





	public ArrayList<String> getIdTarget() {
		return idTarget;
	}



	public Class<?> getClase() {
		return clase;
	}



	public void setClase(Class<?> clase) {
		this.clase = clase;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int compare(WElement wElement) {
		
		return id.compareTo(wElement.getId());
	}

	

	

}
