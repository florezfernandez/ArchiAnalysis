package co.edu.uniandes.archimate.analysis.quantitative.chain.bottleneck;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.quantitative.chain.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class BottleneckResultCAT2 implements ITableResult{
	private String name;
	private int count;
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public BottleneckResultCAT2(String name, int count){
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
