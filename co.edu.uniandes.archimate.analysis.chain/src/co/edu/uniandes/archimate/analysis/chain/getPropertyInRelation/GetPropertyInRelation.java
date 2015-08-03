package co.edu.uniandes.archimate.analysis.chain.getPropertyInRelation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.matrix.Matrix;
import co.edu.uniandes.archimate.analysis.chain.matrix.WElement;
import co.edu.uniandes.archimate.analysis.chain.utilities.results.ResultSerializable;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CatalogSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.editor.diagram.ArchimateDiagramEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.AssignmentRelationship;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessFunction;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class GetPropertyInRelation extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{

	private List<GetPropertyInRelationResult> results1;
	private IArchimateElement element1;
	private String pName;
	private String pValue;
	private String[] results;


	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Get Property";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@SuppressWarnings("null")
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
		EList<IProperty> propertiesList=element1.getProperties();
		for(IProperty property:propertiesList){
			String key=property.getKey();
			String value=property.getValue();
			if(key.equals(pName)){
				pValue=value;
			}
		}

		GetPropertyInRelationResult result=new GetPropertyInRelationResult(pName,pValue);
		results1.add(result);
		return null;
	}



	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{

		String[] headers = new String[]{"Property", "Value"};
		int[] widths = new int[]{200, 200};
		return createTable(headers, widths, results1, true, true);

	} 
	/**
	 * 
	 */
	@Override
	public Boolean clearResults() {
		return true;
	}

	@Override
	public String[] displayWidget() {
		CatalogSWT cSWT= new CatalogSWT(Display.getCurrent());
		cSWT.displayWidget();
		String[] ret={cSWT.getFile(),cSWT.getnClass()};
		return ret;

	}

	@Override
	public String getDescription() {
		return "This function returns property of the specified relationship.\n"
				+ "Input parameters:\n"
				+ "1. Id of the element\n"
				+ "2. Name of the property\n"
				+ "Output:\n"
				+ "1. Property value";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {

		initializeForChain();
		if(params.length!=2){
			throw new Exception("Wrong number of parameters");
		}
		String relationID=params[0];
		ArrayList<String> list=new ArrayList<String>();
		pName=params[1];
		if(relationID==null){
			throw new Exception("Id can't be null");
		}
		if(pName==null){
			throw new Exception("Property name can't be null");
		}

		results1 = new ArrayList<GetPropertyInRelationResult>();
		ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();
		List<IRelationship> relationships = getRelations(IRelationship.class);
		for(IRelationship relationship:relationships){
			if(relationship.getId().equals(relationID)){
				list1.add(relationship);
			}

		}
		element1=list1.get(0);
		executeFunction();
		results=new String[1];
		results[0]=pValue;
		return results;
	}



	@Override
	public String[] inNames() {

		return new String[]{"Relarionship ID:","Property"};
	}

	@Override
	public String[] outNames() {
		return new String[]{"Property Id"};
	}


	/**
	 * 
	 * @param diagramModelObj
	 * @param elementList
	 * @param idsCat1 
	 * @throws Exception
	 */
	private void searchElementsRecursively(IDiagramModelObject diagramModelObj, List<IArchimateElement> elementList,ArrayList<String> idsCat) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursively(diagramModelObjChild, elementList, idsCat);
			}

			if(idsCat.contains(archimateObject.getArchimateElement().getId())){
				elementList.add((IArchimateElement) DiagramModelUtil.getModelElement(diagramModelObj));
			}			
		}
	}

}