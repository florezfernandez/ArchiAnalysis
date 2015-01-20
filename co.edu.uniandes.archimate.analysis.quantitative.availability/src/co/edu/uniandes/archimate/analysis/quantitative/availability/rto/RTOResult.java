package co.edu.uniandes.archimate.analysis.quantitative.availability.rto;

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Image;

import co.edu.uniandes.archimate.analysis.ITableResult;

import com.archimatetool.model.IArchimateElement;

public class RTOResult implements ITableResult{
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,##0.000");

	public IArchimateElement element;
	public String categorization;
	public String rto;

	
	public RTOResult(IArchimateElement element)
	{
		this.element = element;
	}
	
	@Override
	public Object[] toArray(){
		return new Object[]{
				element.getClass().getName().replaceFirst(element.getClass().getPackage().getName()+".", ""),
				element.getName(),
				categorization,
				rto,
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
