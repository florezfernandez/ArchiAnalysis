package co.edu.uniandes.archimate.analysis.chain.matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.catalog.CatalogResult;
import co.edu.uniandes.archimate.analysis.chain.utilities.results.ResultSerializable;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.MatrixSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class Matrix extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{
	private List<MatrixResult> listResults;
	private HashMap<String,WElement> elements1;
	private HashMap<String,WElement> elements2;
	private List<WElement> orderedElements2;
	private Class<?> xRelationshipClass;
	private ResultSerializable cat1;
	private ResultSerializable cat2;
	private File saveFile;
	private String[] results;
	private String[] params;

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Matrix";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		MatrixSWT mSWT=new MatrixSWT(Display.getCurrent());
		mSWT.displayWidget();
		String result1=mSWT.getFile1();
		String result2=mSWT.getFile2();
		String fSave=mSWT.getFileSave();
		String nClass=mSWT.getnClass();
		if(result1==null){
			//Validation error
			return null;
		}
		if(result2==null){
			//Validation error
			return null;
		}
		if(fSave!=null){
			saveFile=new File(fSave);
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
		}
		if(nClass==null){
			//Validation error
			return null;
		}
		File file;
		File file2;
		file = new File(result1);
		file2=new File(result2);
		if(!file.exists()||!file2.exists()){
			//Validation Error
			return null;
		}
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			cat1 = (ResultSerializable) in.readObject();
			fileIn = new FileInputStream(file2);
			in = new ObjectInputStream(fileIn);
			cat2 = (ResultSerializable) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException i)
		{
			i.printStackTrace();
		}catch(ClassNotFoundException c)
		{
			System.out.println("class not found");
			c.printStackTrace();
		}
		List<CatalogResult> listR1=null;
		List<CatalogResult> listR2=null;
		if(cat1!=null&&cat2!=null){

			listR1=(List<CatalogResult>)cat1.getResultEntries();
			listR2=(List<CatalogResult>)cat2.getResultEntries();

		
		}
		else{
			//Validation Error
			return null;
		}
		ArrayList<String> idsCat1=new ArrayList<String>();
		ArrayList<String> idsCat2=new ArrayList<String>();

		for(CatalogResult catRes:listR1){
			idsCat1.add(catRes.getId());
		}
		for(CatalogResult catRes:listR2){
			idsCat2.add(catRes.getId());
		}
		
		
		
		listResults = new ArrayList<MatrixResult>();
		elements1 =new HashMap<String,WElement>();
		elements2 =new HashMap<String,WElement>();
		orderedElements2=new ArrayList<WElement>();
		//element1Class=BusinessActor.class;
		//element2Class=BusinessFunction.class;
		xRelationshipClass=Class.forName(nClass);
		ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();
		ArrayList<IArchimateElement> list2=new ArrayList<IArchimateElement>();

		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
			searchElementsRecursively(diagramModelObj, list1,idsCat1);
			searchElementsRecursively(diagramModelObj, list2,idsCat2);
		}
		for(IArchimateElement element:list1){
			WElement wElement=new WElement(element);
			elements1.put(wElement.getId(),wElement);
		}
		for(IArchimateElement element:list2){
			WElement wElement=new WElement(element);
			elements2.put(wElement.getId(),wElement);
			orderedAdd(wElement, orderedElements2);

		}
		@SuppressWarnings("unchecked")
		List<IRelationship> relationships = getRelations(IRelationship.class);
		for(IRelationship relationship:relationships){
			if(relationship.getClass().equals(xRelationshipClass)){
				String idSource=relationship.getSource().getId();
				String idTarget=relationship.getTarget().getId();
				if(elements1.get(idSource)!=null){
					elements1.get(idSource).getIdTarget().add(idTarget);
				}
				
			}


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
			MatrixResult result=new MatrixResult(element, orderedElements2);
			listResults.add(result);
		}

		return null;
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

	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{
			int length=orderedElements2.size();
			String[] headers=new String[length+1];
			int[] widths = new int[length+1];
			
			headers[0]="     ";
			widths[0]=100;
			for(int i=1;i<length+1;i++){
				headers[i]=orderedElements2.get(i-1).getName();
				widths[i]=100;
			}
			save();

			return createTable(headers, widths, listResults, true, true);
		

	}

	private void orderedAdd(WElement element, List<WElement> list){
		if(list.size()==0){
			list.add(element);
		}
		else{
			boolean fin=false;
			for(int i=0;i<list.size()&&!fin;i++){
				if(element.compare(list.get(i))>0){
					list.add(i, element);
					fin=true;
				}
			}
			if(!fin){
				list.add(element);
			}
		}
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
		MatrixSWT mSWT=new MatrixSWT(Display.getCurrent());
		mSWT.displayWidget();
		String[] ret=new String[4];
		ret[0]=mSWT.getFile1();
		ret[1]=mSWT.getFile2();
		ret[2]=mSWT.getFileSave();
		ret[3]=mSWT.getnClass();
		return ret;
	}

	@Override
	public String getDescription() {
		return "This function returns a relation-matrix between two catalogs and the specified relationship.\n"
				+ "Input parameters:\n"
				+ "1. File path of the first catalog\n"
				+ "2. File path of the second catalog\n"
				+ "3. File path where the matrix will be saved\n"
				+ "4. Name of the relationship for the matrix\n"
				+ "Output:\n"
				+ "1. File path where the matrix was saved";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		
		initializeForChain();
		
		if(params.length!=4){
			throw new Exception("Wrong number of parameters");
		}
		String result1=params[0];
		String result2=params[1];
		String fSave=params[2];
		String nClass=params[3];
		if(result1==null){
			throw new Exception("First Catalog can't be null");
		}
		if(result2==null){
			throw new Exception("Second Catalog can't be null");
		}
		if(fSave!=null){
			saveFile=new File(fSave);
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
		}
		if(nClass==null){
			throw new Exception("Relationship can't be null");
		}
		File file;
		File file2;
		file = new File(result1);
		file2=new File(result2);
		if(!file.exists()||!file2.exists()){
			throw new Exception("One of the catalog files is missing");
		}
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			cat1 = (ResultSerializable) in.readObject();
			fileIn = new FileInputStream(file2);
			in = new ObjectInputStream(fileIn);
			cat2 = (ResultSerializable) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException i)
		{
			i.printStackTrace();
		}catch(ClassNotFoundException c)
		{
			System.out.println("class not found");
			c.printStackTrace();
		}
		List<CatalogResult> listR1=null;
		List<CatalogResult> listR2=null;
		if(cat1!=null&&cat2!=null){

			listR1=(List<CatalogResult>)cat1.getResultEntries();
			listR2=(List<CatalogResult>)cat2.getResultEntries();

		
		}
		else{
			throw new Exception("Error loading catalogs");
		}
		ArrayList<String> idsCat1=new ArrayList<String>();
		ArrayList<String> idsCat2=new ArrayList<String>();

		for(CatalogResult catRes:listR1){
			idsCat1.add(catRes.getId());
		}
		for(CatalogResult catRes:listR2){
			idsCat2.add(catRes.getId());
		}
		
		
		
		listResults = new ArrayList<MatrixResult>();
		elements1 =new HashMap<String,WElement>();
		elements2 =new HashMap<String,WElement>();
		orderedElements2=new ArrayList<WElement>();
		//element1Class=BusinessActor.class;
		//element2Class=BusinessFunction.class;
		xRelationshipClass=Class.forName(nClass);
		ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();
		ArrayList<IArchimateElement> list2=new ArrayList<IArchimateElement>();

		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
			searchElementsRecursively(diagramModelObj, list1,idsCat1);
			searchElementsRecursively(diagramModelObj, list2,idsCat2);
		}
		for(IArchimateElement element:list1){
			WElement wElement=new WElement(element);
			elements1.put(wElement.getId(),wElement);
		}
		for(IArchimateElement element:list2){
			WElement wElement=new WElement(element);
			elements2.put(wElement.getId(),wElement);
			orderedAdd(wElement, orderedElements2);

		}
		@SuppressWarnings("unchecked")
		List<IRelationship> relationships = getRelations(IRelationship.class);
		for(IRelationship relationship:relationships){
			if(relationship.getClass().equals(xRelationshipClass)){
				String idSource=relationship.getSource().getId();
				String idTarget=relationship.getTarget().getId();
				WElement we=elements1.get(idSource);
				if(we!=null){
					we.getIdTarget().add(idTarget);
				}
				
			}


		}
		executeFunction();
		save();
		String[] ret=new String[1];
		ret[0]=fSave;
		return ret;
	}

	private void save() {
		int length=orderedElements2.size();
		String[] headers=new String[length+1];
		int[] widths = new int[length+1];
		
		headers[0]="     ";
		widths[0]=100;
		for(int i=1;i<length+1;i++){
			headers[i]=orderedElements2.get(i-1).getName();
			widths[i]=100;
		}
		ResultSerializable resultSerializable=new ResultSerializable(listResults, widths, headers);
		if(saveFile!=null){
			try
			{

				FileOutputStream fileOut =new FileOutputStream(saveFile);
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
		// TODO Auto-generated method stub
		return new String[]{"Catalog 1;","Catalog 2:", "File path: ","Relationship: "};
	}

	@Override
	public String[] outNames() {
		// TODO Auto-generated method stub
		return new String[]{"Matrix id"};
	}
}