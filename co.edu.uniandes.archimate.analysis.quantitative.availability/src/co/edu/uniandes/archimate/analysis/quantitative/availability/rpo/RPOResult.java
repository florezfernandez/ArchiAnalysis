package co.edu.uniandes.archimate.analysis.quantitative.availability.rpo;

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Image;

import co.edu.uniandes.archimate.analysis.ITableResult;

import com.archimatetool.model.IArchimateElement;

public class RPOResult implements ITableResult{
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,##0.000");

	public IArchimateElement element;
	public String categorization;
	public String rpo;

	
	public RPOResult(IArchimateElement element)
	{
		this.element = element;
	}
	
	@Override
	public Object[] toArray(){
		return new Object[]{
				element.getClass().getName().replaceFirst(element.getClass().getPackage().getName()+".", ""),
				element.getName(),
				categorization,
				rpo,
		};
	}

	@Override
	public Image getImage(int index) {
		return null;
	}

	@Override
	public IArchimateElement getElement() {
		return element;
	}
}
