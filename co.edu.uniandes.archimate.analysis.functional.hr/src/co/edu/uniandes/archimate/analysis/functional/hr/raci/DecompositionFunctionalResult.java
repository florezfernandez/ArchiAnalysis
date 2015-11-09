package co.edu.uniandes.archimate.analysis.functional.hr.raci;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;

import com.archimatetool.model.IArchimateElement;

public class DecompositionFunctionalResult implements ITableResult, Serializable{

	private String name;
	private Object [] result;
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public DecompositionFunctionalResult(WElement element1, List<WElement> orderedElements2){
		name=element1.getName();
		result=new Object[orderedElements2.size()+1];
		result[0]=name;
		for(int i=1;i<result.length;i++){
			result[i]="";
		}
		
		Iterator it = element1.getIdSource().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        for(int i=0;i<orderedElements2.size();i++){
				WElement element2=orderedElements2.get(i);
				if(element2.getId().equals(pair.getKey())){
					result[i+1]=pair.getValue();
				}
				else{
					if(result[i+1]==null){
						result[i+1]="";
					}
					
				}
			}
	    }
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#getImage(int)
	 */
	@Override
	public Image getImage(int index) {
		
		
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
	
}
