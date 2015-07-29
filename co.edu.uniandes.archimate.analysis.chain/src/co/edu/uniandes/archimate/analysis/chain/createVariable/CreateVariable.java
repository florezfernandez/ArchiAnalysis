package co.edu.uniandes.archimate.analysis.chain.createVariable;

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

public class CreateVariable extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Create Variable";
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
		return "This function creates a variable.\n"
				+ "Input parameters:\n"
				+ "1. Value\n"
				+ "Output:\n"
				+ "1. Value of variable";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		
		initializeForChain();
		
		if(params.length!=1){
			throw new Exception("Wrong number of parameters");
		}
		String n1=params[0];
		String[] ret=new String[1];
		ret[0]=n1;
		return ret;
	}


	@Override
	public String[] inNames() {
		// TODO Auto-generated method stub
		return new String[]{"Number 1: "};
	}

	@Override
	public String[] outNames() {
		// TODO Auto-generated method stub
		return new String[]{"Value"};
	}
}