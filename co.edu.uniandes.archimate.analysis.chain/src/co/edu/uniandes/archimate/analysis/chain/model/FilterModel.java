package co.edu.uniandes.archimate.analysis.chain.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.impl.ArchimateDiagramModel;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

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
	
	@Override
	public String getName(){
		return "Get Catalog";
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

		if(elements==null){
			//Validation error
			return null;
		}
		
		//////////MODEL INPUT!!!//////////
		IArchimateModel model = getActiveArchimateModel();
		for (IDiagramModel diag: model.getDiagramModels()) {
			if(diag.getName().equals("HR Capacity")){
				this.setDiagramModel(diag);
				this.setGfViwer(EditorManager.openDiagramEditor(getDiagramModel()).getGraphicalViewer());
			}
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

		/// ELEMENTS SEARCH
		
//		for (int i = 0; i < elementArray.length; i++) {
//			element1Class=Class.forName("com.archimatetool.model.impl."+elementArray[i].trim());
//			ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();
//			for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
//				searchElementsRecursively(diagramModelObj, list1, element1Class);
//			}
//			for(IArchimateElement element:list1){
//				WElement wElement=new WElement(element);
//				elements1.put(wElement.getId(),wElement);
//			}
//		}		
//		
//		System.out.println("------------------ELEMENTS------------------------");
//		Collection<WElement> elements=(Collection<WElement>) elements1.values();
//		for(WElement element:elements){
//			System.out.println(element.getId() + "=== " + element.getName());
//		}
//		
//		///////MODEL OUTPUT!!!///////////
//		
//		IArchimateModel model = getActiveArchimateModel();
//		IDiagramModel diag= (ArchimateDiagramModel) IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
//		diag.setName("Test");
//		diag.setConnectionRouterType(0);
//		diag.eAdapters().addAll(model.getDiagramModels().get(0).eAdapters());
//		model.getDiagramModels().add(diag);
//		
//		model.getFolder(FolderType.DIAGRAMS).getElements().add(diag);
//
//		/// PRUEBAS PARA AGREGAR ELEMENTO ///
//		
//		DiagramModelArchimateObject diagramModelObject= (DiagramModelArchimateObject) IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
////		diagramModelObject.getChildren().add((IDiagramModelObject) IArchimateFactory.eINSTANCE.createBusinessActor());
//
////		Class<?> clazz=  diagramModelObject.getClass();
////		Field f = clazz.getDeclaredField("fArchimateElement");
////		f.setAccessible(true);
////		f.set(diagramModelObject, IArchimateFactory.eINSTANCE.createBusinessActor()); 
////		
////		System.out.println("TEST: " + DiagramModelUtil.getModelElement(diagramModelObject).getClass().toString());
////		
////		diag.getChildren().add(diagramModelObject);
//		
//		this.setDiagramModel(diag);
//		this.setGfViwer(EditorManager.openDiagramEditor(getDiagramModel()).getGraphicalViewer());

		
		
		IArchimateModel model = getActiveArchimateModel();
		IDiagramModel diag= (ArchimateDiagramModel) IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
		diag.setName("Test");
		diag.setConnectionRouterType(0);
		diag.eAdapters().addAll(model.getDiagramModels().get(0).eAdapters());
		model.getFolder(FolderType.DIAGRAMS).getElements().add(diag);
//		diag.getChildren().addAll(this.getDiagramModel().getChildren());
		
		for (int i = 0; i < elementArray.length; i++) {
			element1Class=Class.forName("com.archimatetool.model.impl."+elementArray[i].trim());
			ArrayList<IDiagramModelObject> list1=new ArrayList<IDiagramModelObject>();
			for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
				copyElementsRecursively(diagramModelObj,list1, element1Class);
			}
		}	
		
		for (int i = 0; i < elementArray.length; i++) {
			element1Class=Class.forName("com.archimatetool.model.impl."+elementArray[i].trim());
			ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();
			for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
				searchElementsRecursively(diagramModelObj, list1, element1Class);
			}
			for(IArchimateElement element:list1){
				WElement wElement=new WElement(element);
				elements1.put(wElement.getId(),wElement);
			}
		}	
		
		System.out.println("------------------ELEMENTS------------------------");
		Collection<WElement> elements=(Collection<WElement>) elements1.values();
		for(WElement element:elements){
			System.out.println(element.getId() + "=== " + element.getName());
		}
		
		this.setDiagramModel(diag);
		this.setGfViwer(EditorManager.openDiagramEditor(getDiagramModel()).getGraphicalViewer());
		
		return null;
	}
	
	/**
	 * 
	 * @param diagramModelObj
	 * @param elementList
	 * @throws Exception
	 */
	private void searchElementsRecursively(IDiagramModelObject diagramModelObj, List<IArchimateElement> elementList,Class<?> elementClass) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursively(diagramModelObjChild, elementList, elementClass);
			}

			if(DiagramModelUtil.getModelElement(diagramModelObj).getClass().equals(elementClass)){
				elementList.add((IArchimateElement) DiagramModelUtil.getModelElement(diagramModelObj));
			}			
		}
	}
	
	private void copyElementsRecursively(IDiagramModelObject diagramModelObj, List<IDiagramModelObject> elementList, Class<?> elementClass) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				copyElementsRecursively(diagramModelObjChild, elementList, elementClass);
			}

			if(DiagramModelUtil.getModelElement(diagramModelObj).getClass().equals(elementClass)){
				elementList.add(diagramModelObj);
			}			
		}
	}
	
	private void deleteElementsRecursively(IDiagramModelObject diagramModelObj, List<IArchimateElement> elementList,Class<?> elementClass) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursively(diagramModelObjChild, elementList, elementClass);
			}

			if(DiagramModelUtil.getModelElement(diagramModelObj).getClass().equals(elementClass)){
				elementList.add((IArchimateElement) DiagramModelUtil.getModelElement(diagramModelObj));
			}			
		}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] inNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] outNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
