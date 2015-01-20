package co.edu.uniandes.archimate.analysis.quantitative.availability.rpo;

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

public class RPO extends AbstractArchiAnalysisFunction{

	private List<RPOResult> aResult;
	private List<WService> wElements;

	@Override
	public String getName(){return "Estimate overall architecture RPO";}
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

				wElements.add(wService);
			}

		}else{
			validationResponse.addError("No any service element in the view has the required property: categorization.");
		}

		return null;
	}	

	@Override
	public Object executeFunction() throws Exception {
		aResult = new  ArrayList<RPOResult>();
		for (WService wService : wElements) {
			RPOResult a = new RPOResult(wService.getElement());
			a.categorization = wService.getCategorization();
			a.rpo = Utils.getRPO(wService);
			aResult.add(a);			
		}
		return null;
	}

	@Override
	public Object showResults () throws Exception{
		String[] headers = new String[]{"Element Type","Element","Categorization","Recovery point objective (max. data loss)"};
		int[] widths = new int[]{180,180,180,250};
		createTable(headers, widths, this.aResult, true, true );
		return null;
	}

	@Override
	public Boolean clearResults() {
		return true;
	}




}
