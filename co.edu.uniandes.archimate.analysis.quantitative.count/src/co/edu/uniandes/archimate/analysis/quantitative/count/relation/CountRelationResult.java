package co.edu.uniandes.archimate.analysis.quantitative.count.relation;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.quantitative.count.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

/**
 * 
 * @author Hernan
 */
public class CountRelationResult implements ITableResult{
	private Class<?> relationType;
	private int relationAmount;
	
	/**
	 * 
	 * @param relationType
	 * @param line
	 */
	public CountRelationResult(Class<?> relationType, int relationAmount){
		this.relationType = relationType;
		this.relationAmount = relationAmount;
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
		return new Object[]{relationType.getSimpleName(), Integer.toString(relationAmount)};
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
