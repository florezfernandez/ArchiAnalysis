package co.edu.uniandes.archimate.analysis.chain.catalog;

import java.io.Serializable;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.chain.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class CatalogResultAttribute extends CatalogResult{
	private String name;
	private String id;
//	private String[] attributes;
	Object[] toArray;
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public CatalogResultAttribute(String name,String id, String[] attributes){
		super(name, id);
		this.name=name;
		this.id=id;
//		this.attributes=attributes;
//		
//		for(int i=0; i<this.attributes.length; i++){
//			System.out.println("ResAt init: " + this.attributes[i]);
//		}
		
		toArray= new Object[attributes.length+2];
		toArray[0]=name;
		toArray[1]=id;
		for(int i=0; i<attributes.length; i++){
			System.out.println("ResAt: " + attributes[i]);
			toArray[i+2]= attributes[i];
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
//		Object[] ob= new Object[attributes.length+2];
//		ob[0]=name;
//		ob[1]=id;
//		System.out.println("............ Name: " + name + " ...............");
//		for(int i=0; i<attributes.length; i++){
//			System.out.println("ResAt: " + attributes[i]);
//			ob[i+2]= attributes[i];
//		}
//		return ob;
		
		return toArray;
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#getImage(int)
	 */
	@Override
	public Image getImage(int index) {
//		if(index > 0){
//			return AbstractUIPlugin.imageDescriptorFromPlugin(AnalysisPlugin.PLUGIN_ID, "icons/sum_icon_16.png").createImage();
//		}
//		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.IResult#getElement()
	 */
	@Override
	public IArchimateElement getElement() {
		return null;
	}

	public String getId() {
		return id;
	}
	
	
}
