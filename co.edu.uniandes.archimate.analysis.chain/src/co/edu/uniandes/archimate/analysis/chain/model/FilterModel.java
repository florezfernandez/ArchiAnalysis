package co.edu.uniandes.archimate.analysis.chain.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.impl.ArchimateDiagramModel;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.DiagramModelArchimateConnection;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.impl.DiagramModelConnection;
import com.archimatetool.model.impl.DiagramModelObject;
import com.archimatetool.model.impl.Relationship;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.matrix.WElement;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CatalogAttributeSWT;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.FilterModelSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

public class FilterModel extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{

	private HashMap<String,WElement> elements1;
	private Class<?> element1Class;
	private File file;
	private String[] params;
	private String[] results;
	private String[] elementArray;
	private String name;
	
	@Override
	public String getName(){
		return "Get Filter Model";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@SuppressWarnings("null")
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		FilterModelSWT fmSWT= new FilterModelSWT(Display.getCurrent());
		fmSWT.displayWidget();

		String elements=fmSWT.getElements();
		name= fmSWT.getNewModelName(); 

		if(elements==null){
			//Validation error
			return null;
		}
		if(name==null){
			//Validation error
			return null;
		}
		
		elementArray = elements.split(",");
		elements1 = new HashMap<String,WElement>();
		
		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {
		IArchimateModel model = getActiveArchimateModel();
		IDiagramModel diag= (ArchimateDiagramModel) IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
		diag.setName(name);
		diag.setConnectionRouterType(0);
		diag.eAdapters().addAll(model.getDiagramModels().get(0).eAdapters());
		model.getFolder(FolderType.DIAGRAMS).getElements().add(diag);
		
		for (int i = 0; i < elementArray.length; i++) {
			element1Class=Class.forName("com.archimatetool.model.impl."+elementArray[i].trim());
			ArrayList<IDiagramModelObject> list1=new ArrayList<IDiagramModelObject>();
			for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
				copyElementsRecursively(diagramModelObj,list1, element1Class);
			}
			diag.getChildren().addAll(list1);
		}
		getRelationsNewModel(this.getDiagramModel().getChildren(), diag.getChildren());
		
		this.setDiagramModel(diag);
		this.setGfViwer(EditorManager.openDiagramEditor(getDiagramModel()).getGraphicalViewer());

		return null;
	}
	
	/**
	 * 
	 * @param diagramModelObj
	 * @param elementList
	 * @param elementClass
	 * @throws Exception
	 */
	private void copyElementsRecursively(IDiagramModelObject diagramModelObj, List<IDiagramModelObject> elementList, Class<?> elementClass) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				copyElementsRecursively(diagramModelObjChild, elementList, elementClass);
			}

			if(DiagramModelUtil.getModelElement(diagramModelObj).getClass().equals(elementClass)){
				DiagramModelArchimateObject diaModelArchimateObject= (DiagramModelArchimateObject) diagramModelObj.getCopy();
				ArchimateElement archiElement= (ArchimateElement) ((DiagramModelArchimateObject) diagramModelObj).getArchimateElement().getCopy();
				archiElement.setId(((DiagramModelArchimateObject) diagramModelObj).getArchimateElement().getId());
				diaModelArchimateObject.setArchimateElement(archiElement);
				diaModelArchimateObject.setId(diagramModelObj.getId());
				
				elementList.add(diaModelArchimateObject);
			}			
		}
	}
	
	private void getRelationsNewModel(List<IDiagramModelObject> diagramModelObjList, List<IDiagramModelObject> diaModelArchimateObjectList) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException{
		DiagramModelArchimateObject diaModelArchimateObject=null;
		for (IDiagramModelObject diagramModelObj : diagramModelObjList) {
			for (IDiagramModelConnection diaModelArchimateConnection : (List<IDiagramModelConnection>) diagramModelObj.getSourceConnections()) {
			IDiagramModelConnection diagConnection = validSourceConnection(diaModelArchimateConnection, diaModelArchimateObjectList);
			if(diagConnection!=null){

				for (IDiagramModelObject diagMod : diaModelArchimateObjectList) {
					if(diaModelArchimateConnection.getSource().getId().equals(diagMod.getId())){
						diaModelArchimateObject= (DiagramModelArchimateObject) diagMod;
						diagConnection.setSource((DiagramModelArchimateObject) diagMod);
					}
				}
				
				diagConnection.setId(diaModelArchimateConnection.getId());
				diagConnection.setName(diaModelArchimateConnection.getName());
				Relationship relationship= (Relationship) ((DiagramModelArchimateConnection) diaModelArchimateConnection).getRelationship().getCopy();
				relationship.setId(((DiagramModelArchimateConnection) diaModelArchimateConnection).getRelationship().getId());

				((DiagramModelArchimateConnection) diagConnection).setRelationship(relationship);
				
				diaModelArchimateObject.addConnection(diagConnection);
				diagConnection.getTarget().addConnection(diagConnection);
			}
		}
		}
	}
	
	/**
	 * 
	 * @param diaModelArchimateConnection
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private IDiagramModelConnection validSourceConnection(IDiagramModelConnection diaModelArchimateConnection, List<IDiagramModelObject> diaModelArchimateObject) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException{
		for (int i = 0; i < elementArray.length; i++) {
			if(DiagramModelUtil.getModelElement(diaModelArchimateConnection.getTarget()).getClass().equals(Class.forName("com.archimatetool.model.impl."+elementArray[i].trim()))){
				IDiagramModelConnection diagConnection= (IDiagramModelConnection) diaModelArchimateConnection.getCopy();

				for (IDiagramModelObject diagMod : diaModelArchimateObject) {
					if(diaModelArchimateConnection.getTarget().getId().equals(diagMod.getId())){
						diagConnection.setTarget((DiagramModelArchimateObject) diagMod);
					}
				}
				return diagConnection;
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "This function returns a model with the elements of specific classes.\n"
				+ "Input parameters:\n"
				+ "1. Name of the new model\n"
				+ "2. Elements to filter\n"
				+ "Output:\n"
				+ "1. Model ID";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		initializeForChain();
		if(params.length!=2){
			throw new Exception("Wrong number of parameters");
		}
		name= params[0];
		String elements= params[1];
		if(name==null){
			//Validation error
			return null;
		}
		if(elements==null){
			//Validation error
			return null;
		}
		
		elementArray = elements.split(",");
		elements1 = new HashMap<String,WElement>();
		
		executeFunction();
		
		results=new String[1];
		results[0]="";
		return results;
	}

	@Override
	public String[] inNames() {
		return new String[]{"Name","Elements"};
	}

	@Override
	public String[] outNames() {
		return new String[]{"Model ID"};
	}

}
