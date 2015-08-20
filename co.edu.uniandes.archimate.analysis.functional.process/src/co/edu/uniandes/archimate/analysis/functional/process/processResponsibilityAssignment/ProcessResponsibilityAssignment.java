package co.edu.uniandes.archimate.analysis.functional.process.processResponsibilityAssignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.utilities.results.ResultSerializable;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.AssignmentRelationship;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessFunction;
import com.archimatetool.model.impl.BusinessProcess;
import com.archimatetool.model.impl.BusinessRole;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class ProcessResponsibilityAssignment extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{
	
	private List<ProcessResponsibilityAssignmentResult> results = new ArrayList<ProcessResponsibilityAssignmentResult>();
	private List<IRelationship> relationships = new ArrayList<IRelationship>();
	private File file;
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Process responsibility assignment";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		relationships = getRelations(AssignmentRelationship.class);
		List<IArchimateElement> sourceElements = getElements(BusinessActor.class, BusinessRole.class);
		List<IArchimateElement> targetElements = getElements(BusinessProcess.class, BusinessFunction.class);
		if(relationships.isEmpty())
			validationResponse.addError("No any Assignment relationship");
		if(sourceElements.isEmpty())
			validationResponse.addError("No any BusinessActor or BusinessRole");
		if(targetElements.isEmpty())
			validationResponse.addError("No any BusinessProcess or BusinessFunction");
		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {
		IArchimateElement targetElement;
		IArchimateElement sourceElement;
		clearResults();
		for (IRelationship relation : relationships) {
			targetElement= getTargetElementFromRelationsByElement(BusinessProcess.class, relation);
			if(targetElement==null) targetElement= getTargetElementFromRelationsByElement(BusinessFunction.class, relation);
			if(targetElement!=null){
				sourceElement= getSourceElementFromRelationsByElement(BusinessRole.class, relation);
				if(sourceElement==null)	sourceElement= getSourceElementFromRelationsByElement(BusinessActor.class, relation);
				if(sourceElement!=null){
					ProcessResponsibilityAssignmentResult procResult=new ProcessResponsibilityAssignmentResult(targetElement.getId(), targetElement.getName(), sourceElement.getId(), sourceElement.getName());
					results.add(procResult);
				}
			}
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
		String[] headers = new String[]{"Target Id", "Target Name", "Source Id", "Source Name"};
		int[] widths = new int[]{100, 200, 100, 200};
		return createTable(headers, widths, results, true, true);
	}

	/**
	 * 
	 */
	@Override
	public Boolean clearResults() {
		results.clear();
		return true;
	}

	@Override
	public String[] displayWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "This function is used for identify which business process/function \n"
				+ "is assigned to which business role/actor.";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		
		initializeForChain();
		if(params.length!=1){
			throw new Exception("Wrong number of parameters");
		}
		String result=params[0];
		if(result==null){
			throw new Exception("File path can't be null");
		}
		file = new File(result);
		if(!file.exists()){
			file.createNewFile();
		}
		
		relationships = getRelations(AssignmentRelationship.class);
		executeFunction();
		save();
		
		String[] response=new String[1];
		response[0]=result;
		return response;
	}
	
	private void save(){
		String[] headers = new String[]{"Target Id", "Target Name", "Source Id", "Source Name"};
		int[] widths = new int[]{100, 200, 100, 200};
		ResultSerializable resultSerializable=new ResultSerializable(results, widths, headers);
		if(file!=null){
			try
			{
				FileOutputStream fileOut =new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(resultSerializable);
				out.close();
				fileOut.close();
			}catch(IOException i)
			{
				i.printStackTrace();
			}
		}
	}

	@Override
	public String[] inNames() {
		return new String[]{"File path:"};
	}

	@Override
	public String[] outNames() {
		return new String[]{"Process responsibility assignment id:"};
	}
}