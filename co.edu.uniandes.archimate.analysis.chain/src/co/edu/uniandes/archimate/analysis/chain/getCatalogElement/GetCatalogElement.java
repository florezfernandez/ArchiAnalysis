package co.edu.uniandes.archimate.analysis.chain.getCatalogElement;

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

public class GetCatalogElement extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{
	private List<GetCatalogElementResult> listResults;
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
		return "Get Catalog Element";
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
		
//		Collection<WElement> elements=(Collection<WElement>) elements1.values();
//		for(WElement element:elements){
//			GetCatalogElementResult result=new GetCatalogElementResult(element, orderedElements2);
//			listResults.add(result);
//		}

		return null;
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

			return createTable(headers, widths, listResults, true, true);
		

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

		return null;
	}

	@Override
	public String getDescription() {
		return "This function returns the element in the catalog in the specified index.\n"
				+ "Input parameters:\n"
				+ "1. File path of the catalog\n"
				+ "2. Index of the element\n"
				+ "Output:\n"
				+ "1. Id of the element";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		
		initializeForChain();
		
		if(params.length!=2){
			throw new Exception("Wrong number of parameters");
		}
		String catalog=params[0];
		String index=params[1];
		if(catalog==null){
			throw new Exception("Catalog can't be null");
		}
		if(index==null){
			throw new Exception("Index can't be null");
		}
		File file;
		file = new File(catalog);
		if(!file.exists()){
			throw new Exception("Catalog file is missing");
		}
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			cat1 = (ResultSerializable) in.readObject();
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
		if(cat1!=null){

			listR1=(List<CatalogResult>)cat1.getResultEntries();
		}
		else{
			throw new Exception("Error loading catalogs");
		}
		ArrayList<String> idsCat1=new ArrayList<String>();
		for(CatalogResult catRes:listR1){
			idsCat1.add(catRes.getId());
		}
		
		String[] ret=new String[1];
		int i=Integer.parseInt(index);
		ret[0]=idsCat1.get(i);
		return ret;
	}


	@Override
	public String[] inNames() {
		// TODO Auto-generated method stub
		return new String[]{"Catalog: ","Index: "};
	}

	@Override
	public String[] outNames() {
		// TODO Auto-generated method stub
		return new String[]{"Element Id"};
	}
}