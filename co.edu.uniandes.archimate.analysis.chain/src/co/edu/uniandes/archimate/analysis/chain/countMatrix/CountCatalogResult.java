package co.edu.uniandes.archimate.analysis.chain.countMatrix;

import java.io.Serializable;

import org.eclipse.swt.graphics.Image;

import co.edu.uniandes.archimate.analysis.ITableResult;

import com.archimatetool.model.IArchimateElement;

public class CountCatalogResult implements ITableResult,Serializable{
	private String name;
	private int count;
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public CountCatalogResult(String name, int count){
		this.name=name;
		this.count=count;
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
		return new Object[]{name,""+count};
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
	
	public int getCount(){
		return count;
	}
}
