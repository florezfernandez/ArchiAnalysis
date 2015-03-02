package co.edu.uniandes.archimate.analysis.quantitative.chain.bottleneck;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.quantitative.chain.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class BottleneckResultCAT implements ITableResult{
	private String name;
	private String id;
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public BottleneckResultCAT(String name,String id){
		this.name=name;
		this.id=id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
		return new Object[]{name,id};
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
}
