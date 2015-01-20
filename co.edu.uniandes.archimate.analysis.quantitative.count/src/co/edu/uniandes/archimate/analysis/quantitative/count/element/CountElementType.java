package co.edu.uniandes.archimate.analysis.quantitative.count.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class CountElementType extends AbstractArchiAnalysisFunction{
	private List<CountElementResult> results = new ArrayList<CountElementResult>();
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Count elements by type";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {
		Map<Class<?>, Integer> elementsCount = new HashMap<Class<?>, Integer>();
		Set<EObject> elements = new HashSet<EObject>();
		
		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
			searchElementsRecursively(diagramModelObj, elements);
		}
		
		for(EObject eObject : elements){
			Class<?> eObjectClass = eObject.getClass();
			
			if(!elementsCount.containsKey(eObjectClass)){
				elementsCount.put(eObjectClass, (Integer)1);
			}else{
				Integer currentInstances = elementsCount.get(eObjectClass);
				currentInstances++;
				elementsCount.put(eObjectClass, (Integer)currentInstances);
			}
		}
		
		for(Entry<Class<?>, Integer> entry : elementsCount.entrySet()){
//			System.out.println(entry.getKey().toString() + " - [" + entry.getValue().toString() + "]");
			CountElementResult elementResult = new CountElementResult(entry.getKey(), entry.getValue());
			results.add(elementResult);
		}
		
	    return null;
	}
	
	/**
	 * 
	 * @param diagramModelObj
	 * @param elements
	 * @throws Exception
	 */
	private void searchElementsRecursively(IDiagramModelObject diagramModelObj, Set<EObject> elements) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);
			
			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursively(diagramModelObjChild, elements);
			}
			
			elements.add(DiagramModelUtil.getModelElement(diagramModelObj));
		}
	}
	
	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"ElementType", "Amount"};
		int[] widths = new int[]{300, 200};
		return createTable(headers, widths, results, true, true);
	}

	/**
	 * 
	 */
	@Override
	public Boolean clearResults() {
		return true;
	}
}