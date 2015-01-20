package co.edu.uniandes.archimate.analysis.quantitative.availability.percentage;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.AnalysisProperty;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedElement;
import co.edu.uniandes.archimate.analysis.quantitative.availability.Utils;
import co.edu.uniandes.archimate.analysis.quantitative.availability.WService;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.impl.ApplicationService;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.BusinessService;
import com.archimatetool.model.impl.InfrastructureService;

public class AvailabilityPercentage extends AbstractArchiAnalysisFunction{

	private List<AvailabilityPercentageResult> aResult;
	private List<WService> wElements;

	@Override
	public String getName(){return "Estimate overall architecture availability %";}
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		wElements = new ArrayList<WService>();
		//get all Service entities that contain the required property: categorization.
		List<IArchimateElement> listComponents = getWeakTypedElements(
				new WeakTypedElement(BusinessService.class, new AnalysisProperty("categorization",DiagramModelUtil.DEFFAUL_VALUE)),
				new WeakTypedElement(ApplicationService.class, new AnalysisProperty("categorization",DiagramModelUtil.DEFFAUL_VALUE)),
				new WeakTypedElement(InfrastructureService.class, new AnalysisProperty("categorization",DiagramModelUtil.DEFFAUL_VALUE))
				);
		if(listComponents.size()>0)
		{
			for (IArchimateElement element : listComponents) {
				WService wService = new WService((ArchimateElement)element);

				wService.setCategorization(DiagramModelUtil.getValue(element, "categorization"));
				if(!wService.isValidCategorization()) validationResponse.addError("Service categorization has a wrong value. The valid values are: " + wService.getCategorizationValidValues() ,element);

				wService.setHoursXdays(DiagramModelUtil.getValue(element, "hoursXdays"));
				if(wService.getHoursXdays() == null) validationResponse.addError("Service Service hours per day availability (hoursXdays) is not set or has a wrong value. e.g. 24x7, 15x5", element);

				wService.setMaxRecTimeMins(DiagramModelUtil.getDoubleValue(element, "maxRecTimeMins"));
				if(wService.getMaxRecTimeMins() == null) validationResponse.addError("Service expected recovery time in mins (maxRecTimeMins) is not set or has a wrong value.", element);
				
				wElements.add(wService);
			}

		}else{
			validationResponse.addError("No any service element in the view has the required property: categorization.");
		}

		return null;
	}	

	@Override
	public Object executeFunction() throws Exception {
		aResult = new  ArrayList<AvailabilityPercentageResult>();
		for (WService wService : wElements) {
			AvailabilityPercentageResult a = new AvailabilityPercentageResult(wService.getElement());
			a.categorization = wService.getCategorization();
			a.minorOutage = Utils.getMaxMinorOutagexCategorization(wService);
			a.hoursXdays = wService.getHoursXdays();
			a.days = wService.getDays();
			a.hours = wService.getHours();
			a.yearlyTotalHours = a.hours * a.days * Utils.TIME_1YEAR_TO_WEAKS;
			a.montlyTotalHours = a.yearlyTotalHours / Utils.TIME_1YEAR_TO_MONTHS;
			a.availabilityPercentage = 1 - (a.minorOutage/a.montlyTotalHours );
			a.maxRecTimeMins = wService.getMaxRecTimeMins();
			a.yearlyFails = a.yearlyTotalHours / (a.maxRecTimeMins * Utils.TIME_1HOUR_TO_MINS);
			a.montlyFails = a.yearlyFails / Utils.TIME_1YEAR_TO_MONTHS;
			
			aResult.add(a);
		}
		return null;
	}

	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"Element Type","Element","Categorization","minorOutage","hoursXdays",
				"maxRecTimeMins", "yearlyTotalHours", "montlyTotalHours", "% availability", "montlyFails", "yearlyFails"};
		int[] widths = new int[]{120,120,120,90,90,100,100,100,90,90,90};
		createTable(headers, widths, this.aResult, true, true );
		return null;
	}

	@Override
	public Boolean clearResults() {
		return true;
	}




}
