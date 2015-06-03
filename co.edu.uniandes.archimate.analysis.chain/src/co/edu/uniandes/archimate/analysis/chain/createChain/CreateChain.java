package co.edu.uniandes.archimate.analysis.chain.createChain;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXException;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CatalogSWT;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CreateChainSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;

public class CreateChain extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{
	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Create Chain";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{

		CreateChainSWT cSWT= new CreateChainSWT(Display.getCurrent());
		cSWT.displayWidget();

		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {

		

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
		return "";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		
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