package co.edu.uniandes.archimate.analysis.functional.gap.businessprocessgapanalysis;

import org.eclipse.swt.graphics.Image;

import co.edu.uniandes.archimate.analysis.ITableResult;

import com.archimatetool.model.IArchimateElement;

public class BusinessProcessGapAnalysisResult implements ITableResult{
	private String elementID;
	private String elementName;
	private String elementStatus;
	private String elementType;
	
	/**
	 * 
	 * @param elementID
	 * @param elementName
	 * @param elementStatus
	 */
	public BusinessProcessGapAnalysisResult(String elementID,String elementName, String elementStatus,String elementType){
		this.elementID = elementID;
		this.elementName = elementName;
		this.elementStatus=elementStatus;
		this.elementType=elementType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
		return new Object[]{elementID,elementType, elementName,elementStatus};
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
