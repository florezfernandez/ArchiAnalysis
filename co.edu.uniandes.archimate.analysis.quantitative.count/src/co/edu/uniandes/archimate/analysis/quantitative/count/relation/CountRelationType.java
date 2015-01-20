package co.edu.uniandes.archimate.analysis.quantitative.count.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;

import com.archimatetool.model.IRelationship;

public class CountRelationType extends AbstractArchiAnalysisFunction{
	private List<CountRelationResult> results = new ArrayList<CountRelationResult>();
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Count relations by type";
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
	@SuppressWarnings("unchecked")
	public Object executeFunction() throws Exception {
		List<IRelationship> relationships = getRelations(IRelationship.class);
		Map<Class<?>, Integer> relationshipCounter = new HashMap<Class<?>, Integer>();
		
//		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
//			searchElementsRecursively(diagramModelObj, relationships);
//		}
		
		for(IRelationship relationship : relationships){
			Class<?> relationshipClass = relationship.getClass();
			
			if(!relationshipCounter.containsKey(relationshipClass)){
				relationshipCounter.put(relationshipClass, (Integer)1);
			}else{
				Integer currentInstances = relationshipCounter.get(relationshipClass);
				currentInstances++;
				relationshipCounter.put(relationshipClass, (Integer)currentInstances);
			}
		}
		
//		for(IRelationship relationship : relationships){
//			System.out.print("\n:::: Relation name = " + relationship.getName());
//			System.out.print(":::: Relation type = " + relationship.getClass().getSimpleName());
//			System.out.print("\n:::: Relation toString = " + relationship.toString());
//			System.out.println(":::: Relation Id = " + relationship.getId());
//		}
		
		for(Entry<Class<?>, Integer> entry : relationshipCounter.entrySet()){
			CountRelationResult relationResult = new CountRelationResult(entry.getKey(), entry.getValue());
			results.add(relationResult);
		}
		
	    return null;
	}

	/**
	 * 
	 * @param diagramModelObj
	 * @param relationships
	 * @throws Exception
	 */
//	private void searchRelationsRecursively(IDiagramModel diagramModelObj, List<IRelationship> relationships) throws Exception{
//		List<IRelationship> listRelations =  DiagramModelUtil.getCurrentViewRelationsByType(diagramModelObj, IRelationship.class);
//		DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);
//			
//			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
//				searchElementsRecursively(diagramModelObjChild, relationships);
//			}
//			
//			relationships.add(DiagramModelUtil.getModelElement(diagramModelObj));
//		
//		return listRelations;
//	}
	
	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"RelationType", "Amount"};
		int[] widths = new int[]{300, 200};
		return createTable(headers, widths, results, true, true);
	}

	@Override
	public Boolean clearResults() {
		return true;
	}
}