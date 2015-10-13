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

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.matrix.WElement;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CatalogAttributeSWT;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.FilterModelSWT;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.ModelInputSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

public class ModelInput extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{

	private HashMap<String,WElement> elements1;
	private Class<?> element1Class;
	private File file;
	private String[] params;
	private String[] results;
	private String name;
	
	@Override
	public String getName(){
		return "Get Model";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@SuppressWarnings("null")
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{


		ModelInputSWT miSWT= new ModelInputSWT(Display.getCurrent());
		miSWT.displayWidget();

		name= miSWT.getNewModelName().trim();

		if(name==null){
			//Validation error
			return null;
		}
		
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
		for (IDiagramModel diag: model.getDiagramModels()) {
			if(diag.getName().trim().equals(name)){
				this.setDiagramModel(diag);
				this.setGfViwer(EditorManager.openDiagramEditor(getDiagramModel()).getGraphicalViewer());
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
		return "This function returns a model selected by the user.\n"
				+ "Input parameters:\n"
				+ "1. Name of the model to open\n"
				+ "Output:\n"
				+ "1. Model ID";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		initializeForChain();
		if(params.length!=1){
			throw new Exception("Wrong number of parameters");
		}
		name= params[0];
		if(name==null){
			//Validation error
			return null;
		}
		
		elements1 = new HashMap<String,WElement>();
		
		executeFunction();
		
		results=new String[1];
		results[0]="";
		return results;
	}

	@Override
	public String[] inNames() {
		return new String[]{"Name"};
	}

	@Override
	public String[] outNames() {
		return new String[]{"Model ID"};
	}

}
