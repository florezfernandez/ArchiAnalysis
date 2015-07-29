package co.edu.uniandes.archimate.analysis.chain.childrenCatalog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Display;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.catalog.CatalogResult;
import co.edu.uniandes.archimate.analysis.chain.matrix.WElement;
import co.edu.uniandes.archimate.analysis.chain.utilities.results.ResultSerializable;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CatalogSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.impl.DiagramModelObject;

public class ChildrenCatalog extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{

	private List<CatalogResult> results1;
	private HashMap<String,WElement> elements1;
	private Class<?> element1Class;
	private File file;
	private String[] params;
	private String[] results;


	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Get Children Catalog";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@SuppressWarnings("null")
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{


		CatalogSWT cSWT= new CatalogSWT(Display.getCurrent());
		cSWT.displayWidget();

		String result=cSWT.getFile();
		String nClass=cSWT.getnClass();
		if(result==null){
			//Validation error
			return null;
		}
		if(nClass==null){
			//Validation error
			return null;
		}
		file = new File(result);
		if(!file.exists()){
			file.createNewFile();
		}

		elements1 =new HashMap<String,WElement>();
		results1 = new ArrayList<CatalogResult>();
		element1Class=Class.forName(nClass);	
		ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();
		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
			searchElementsRecursivelyById(diagramModelObj, list1,element1Class);
		}
		for(IArchimateElement element:list1){
			WElement wElement=new WElement(element);
			elements1.put(wElement.getId(),wElement);
		}
		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {
		Collection<WElement> elements=(Collection<WElement>) elements1.values();
		for(WElement element:elements){
			CatalogResult result=new CatalogResult(element.getName(),element.getId());
			results1.add(result);
		}
		return null;
	}



	/**
	 * 
	 * @param diagramModelObj
	 * @param elementList
	 * @throws Exception
	 */
	private void searchElementsRecursivelyById(IDiagramModelObject diagramModelObj, List<IArchimateElement> elementList,Class<?> elementClass) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursivelyById(diagramModelObjChild, elementList, elementClass);
			}

			if(DiagramModelUtil.getModelElement(diagramModelObj).getClass().equals(elementClass)){
				elementList.add((IArchimateElement) DiagramModelUtil.getModelElement(diagramModelObj));
			}			
		}
	}

	/**
	 * 
	 * @param diagramModelObj
	 * @param elementList
	 * @throws Exception
	 */
	private void searchElementsRecursivelyById(IDiagramModelObject diagramModelObj, List<IArchimateElement> elementList,String id) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursivelyById(diagramModelObjChild, elementList, id);
			}

			if(archimateObject.getArchimateElement().getId().equals(id)){
				elementList.add((IArchimateElement) DiagramModelUtil.getModelElement(diagramModelObj));
			}			
		}
	}

	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{

		String[] headers = new String[]{"Name", "Element ID"};
		int[] widths = new int[]{200, 200};
		save();
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
		return "This function returns a catalog of the children elements of a specified element.\n"
				+ "Input parameters:\n"
				+ "1. File path where the catalog will be saved\n"
				+ "2. Id of the parent element\n"
				+ "Output:\n"
				+ "1. File path where the catalog was saved";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {

		initializeForChain();
		if(params.length!=2){
			throw new Exception("Wrong number of parameters");
		}
		String result=params[0];
		String id=params[1];
		if(result==null){
			throw new Exception("File path can't be null");
		}
		if(id==null){
			throw new Exception("Element id can't be null");
		}
		file = new File(result);
		if(!file.exists()){
			file.createNewFile();
		}

		elements1 =new HashMap<String,WElement>();
		results1 = new ArrayList<CatalogResult>();	
		ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();

		List<IRelationship> relationships = getRelations(IRelationship.class);
		for(IRelationship relationship:relationships){
			if(relationship.getSource().getId().equals(id)){
				list1.add(relationship.getTarget());
			}

		}
		for(IArchimateElement element:list1){
			WElement wElement=new WElement(element);
			elements1.put(wElement.getId(),wElement);
		}
		executeFunction();
		save();
		results=new String[1];
		results[0]=result;
		return results;
	}

	private void save(){
		String[] headers = new String[]{"Name", "Element ID"};
		int[] widths = new int[]{200, 200};
		ResultSerializable resultSerializable=new ResultSerializable(results1, widths, headers);
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

		return new String[]{"File path:","Element id:"};
	}

	@Override
	public String[] outNames() {
		return new String[]{"Catalog id:"};
	}

}