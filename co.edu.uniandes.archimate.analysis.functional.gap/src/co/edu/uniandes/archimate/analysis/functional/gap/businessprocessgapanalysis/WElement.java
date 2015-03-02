package co.edu.uniandes.archimate.analysis.functional.gap.businessprocessgapanalysis;


import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IProperty;

public class WElement {

	private IArchimateElement element;
	private String id;
	private HashMap<String,WRelation> idSource;
	private HashMap<String,WRelation> idTarget;
	private Class<?> clase;
	private HashMap<String, String> properties;
	public HashMap<String, String> getProperties() {
		return properties;
	}



	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}


	private String model;
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
		idSource=new HashMap<String,WRelation>();
		idTarget=new HashMap<String,WRelation>();
	}

	
	public HashMap<String, WRelation> getIdSource() {
		return idSource;
	}




	public HashMap<String, WRelation> getIdTarget() {
		return idTarget;
	}



	public Class<?> getClase() {
		return clase;
	}



	public void setClase(Class<?> clase) {
		this.clase = clase;
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

	
	public boolean equalProperties(WElement cElement){
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
	
	public boolean equalSource(WElement cElement){
		Set<Entry<String, WRelation>> set= idSource.entrySet();
		for(Entry<String, WRelation> entry:set){
			String key=entry.getKey();
			WRelation value1=entry.getValue();
			WRelation value2=cElement.getIdSource().get(key);
			if(value2==null){
				return false;
			}
			else{
				if(!value1.equals(value2)){
					return false;
				}
			}
		}
		Set<Entry<String, WRelation>> set2= cElement.getIdSource().entrySet();
		for(Entry<String, WRelation> entry:set2){
			String key=entry.getKey();
			WRelation value1=entry.getValue();
			WRelation value2=idSource.get(key);
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
	
	public boolean equalTarget(WElement cElement){
		Set<Entry<String, WRelation>> set= idTarget.entrySet();
		for(Entry<String, WRelation> entry:set){
			String key=entry.getKey();
			WRelation value1=entry.getValue();
			WRelation value2=cElement.getIdTarget().get(key);
			if(value2==null){
				return false;
			}
			else{
				if(!value1.equals(value2)){
					return false;
				}
			}
		}
		Set<Entry<String, WRelation>> set2= cElement.getIdTarget().entrySet();
		for(Entry<String, WRelation> entry:set2){
			String key=entry.getKey();
			WRelation value1=entry.getValue();
			WRelation value2=idTarget.get(key);
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
