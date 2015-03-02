package co.edu.uniandes.archimate.analysis.functional.gap.businessprocessgapanalysis;


import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRelationship;

public class WRelation {

	private IRelationship element;
	private String id;
	private String idSource;
	private String idTarget;
	private Class<?> clase;
	private HashMap<String, String> properties;
	private String model;
	private String name;
	
	
	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}



	public void setIdTarget(String idTarget) {
		this.idTarget = idTarget;
	}
	
	public HashMap<String, String> getProperties() {
		return properties;
	}



	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}



	public Class<?> getClase() {
		return clase;
	}



	public void setClase(Class<?> clase) {
		this.clase = clase;
	}



	public IRelationship getElement() {
		return element;
	}
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}

	public WRelation(IRelationship element) {
		this.element = element;
		setClase(element.getClass());
		properties=new HashMap<String,String>();
		EList<IProperty> propertiesList=element.getProperties();
		for(IProperty property:propertiesList){
			String key=property.getKey();
			String value=property.getValue();
			if(key.equals("id")){
				setId(value);
			}
			else if(key.equals("model")){
				setModel(value);
			}
			else{
				properties.put(key, value);
			}
		}
		name=element.getName();
		idSource=getIdFromElement(element.getSource());
		idTarget=getIdFromElement(element.getTarget());
	}


	
	public String getIdFromElement(IArchimateElement element){
		EList<IProperty> propertiesList=element.getProperties();
		for(IProperty property:propertiesList){
			String key=property.getKey();
			String value=property.getValue();
			if(key.equals("id")){
				return(value);
			}
		}
		return null;
	}

	public String getIdSource() {
		return idSource;
		
	}





	public String getIdTarget() {
		return idTarget;
	}

	
	public String getModel() {
		return model;
	}



	public void setModel(String model) {
		this.model = model;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(WRelation wRelation){
		if(idSource.equals(wRelation.getIdSource())&&idTarget.equals(wRelation.getIdTarget())){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean equalProperties(WRelation cElement){
		Set<Entry<String,String>> set= properties.entrySet();
		for(Entry<String,String> entry:set){
			String key=entry.getKey();
			String value1=entry.getValue();
			String value2=cElement.getProperties().get(key);
			if(value2==null){
				return false;
			}
			else{
				if(!value1.equals(value2)){
					return false;
				}
			}
		}
		Set<Entry<String,String>> set2= cElement.getProperties().entrySet();
		for(Entry<String,String> entry:set2){
			String key=entry.getKey();
			String value1=entry.getValue();
			String value2=properties.get(key);
			if(value2==null){
				return false;
			}
			else{
				if(!value1.equals(value2)){
					return false;
				}
			}
		}
		return true;
	}
	

	
	
}
