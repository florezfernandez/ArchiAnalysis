package co.edu.uniandes.archimate.analysis.quantitative.count.element;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.quantitative.count.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class CountElementResult implements ITableResult{
	private Class<?> elementType;
	private int elementAmount;
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public CountElementResult(Class<?> elementType, int elementAmount){
		this.elementType = elementType;
		this.elementAmount = elementAmount;
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
		return new Object[]{elementType.getSimpleName(), Integer.toString(elementAmount)};
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#getImage(int)
	 */
	@Override
	public Image getImage(int index) {
		if(index > 0){
			return AbstractUIPlugin.imageDescriptorFromPlugin(AnalysisPlugin.PLUGIN_ID, "icons/sum_icon_16.png").createImage();
		}
		
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
