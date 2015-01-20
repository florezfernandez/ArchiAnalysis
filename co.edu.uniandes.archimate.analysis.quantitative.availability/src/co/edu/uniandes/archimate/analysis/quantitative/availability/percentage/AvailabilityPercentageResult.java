package co.edu.uniandes.archimate.analysis.quantitative.availability.percentage;

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Image;

import co.edu.uniandes.archimate.analysis.ITableResult;

import com.archimatetool.model.IArchimateElement;

public class AvailabilityPercentageResult implements ITableResult{
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,##0.000");

	public IArchimateElement element;
	public String categorization;
	public Double minorOutage;
	public String hoursXdays;
	public Double hours;
	public Double days;
	public Double availabilityPercentage;
	public Double montlyTotalHours;
	public Double yearlyTotalHours;
	public Double montlyFails; 
	public Double yearlyFails; 
	public Double maxRecTimeMins;
	
	public AvailabilityPercentageResult(IArchimateElement element)
	{
		this.element = element;
	}
	
	@Override
	public Object[] toArray(){
		return new Object[]{
				element.getClass().getName().replaceFirst(element.getClass().getPackage().getName()+".", ""),
				element.getName(),
				categorization,
				DECIMAL_FORMAT.format(minorOutage),
				hoursXdays,
				maxRecTimeMins,
				yearlyTotalHours,
				montlyTotalHours,
				DECIMAL_FORMAT.format(availabilityPercentage*100)+"%",
				DECIMAL_FORMAT.format(montlyFails),
				DECIMAL_FORMAT.format(yearlyFails)
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
