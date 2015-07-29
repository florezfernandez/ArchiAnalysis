package co.edu.uniandes.archimate.analysis.chain.addition;

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

public class Addition extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Addition";
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

		return null;
	}

	@Override
	public String getDescription() {
		return "This function adds two numbers.\n"
				+ "Input parameters:\n"
				+ "1. First Number\n"
				+ "2. Second Number\n"
				+ "Output:\n"
				+ "1. Id of the result";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		
		initializeForChain();
		
		if(params.length!=2){
			throw new Exception("Wrong number of parameters");
		}
		String n1=params[0];
		String n2=params[1];
		if(n1==null){
			throw new Exception("First number can't be null");
		}
		if(n2==null){
			throw new Exception("Second number can't be null");
		}
		int num1=Integer.parseInt(n1);
		int num2=Integer.parseInt(n2);
		String[] ret=new String[1];
		int i=num1+num2;
		ret[0]=i+"";
		return ret;
	}


	@Override
	public String[] inNames() {
		// TODO Auto-generated method stub
		return new String[]{"Number 1: ","Number 2: "};
	}

	@Override
	public String[] outNames() {
		// TODO Auto-generated method stub
		return new String[]{"Result"};
	}
}