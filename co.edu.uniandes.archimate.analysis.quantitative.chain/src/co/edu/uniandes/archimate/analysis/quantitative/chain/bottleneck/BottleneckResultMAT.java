package co.edu.uniandes.archimate.analysis.quantitative.chain.bottleneck;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.quantitative.chain.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class BottleneckResultMAT implements ITableResult{

	private String name;
	private Object [] result;
	
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public BottleneckResultMAT(WElement element1, List<WElement> orderedElements2){
		name=element1.getName();
		result=new Object[orderedElements2.size()+1];
		result[0]=name;
		for(String target:element1.getIdTarget()){
			for(int i=0;i<orderedElements2.size();i++){
				WElement element2=orderedElements2.get(i);
				if(element2.getId().equals(target)){
					result[i+1]="X";
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
