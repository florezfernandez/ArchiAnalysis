package co.edu.uniandes.archimate.analysis.chain.utilities;


import java.util.ArrayList;

import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.addition.Addition;
import co.edu.uniandes.archimate.analysis.chain.catalog.Catalog;
import co.edu.uniandes.archimate.analysis.chain.catalog.CatalogAttribute;
import co.edu.uniandes.archimate.analysis.chain.catalog.CatalogType;
import co.edu.uniandes.archimate.analysis.chain.catalog.CatalogTypeAttribute;
import co.edu.uniandes.archimate.analysis.chain.childrenCatalog.ChildrenCatalog;
import co.edu.uniandes.archimate.analysis.chain.countMatrix.CountMatrix;
import co.edu.uniandes.archimate.analysis.chain.getCatalogElement.GetCatalogElement;
import co.edu.uniandes.archimate.analysis.chain.getPropertyInRelation.GetPropertyInRelation;
import co.edu.uniandes.archimate.analysis.chain.matrix.Matrix;
import co.edu.uniandes.archimate.analysis.chain.model.FilterModel;
import co.edu.uniandes.archimate.analysis.chain.model.ModelInput;
import co.edu.uniandes.archimate.analysis.chain.relationshipsFromElement.RelationshipsFromElement;
import co.edu.uniandes.archimate.analysis.functional.hr.raci.DecompositionFunctional;
import co.edu.uniandes.archimate.analysis.functional.process.processResponsibilityAssignment.ProcessResponsibilityAssignment;

public class ChainedFunction {
	
	private IChainableArchiFunction function;
	private String[] paramNames;
	private String[] outNames;
	private boolean[] referenced;
	private String[] params;
	private String[] outputs;
	private boolean printable;
	private String name;
	
	public ChainedFunction (){
		
	}
	
	
	
	public void setFunctionClass(String className){
		function=classHandler(className);
		System.out.println("Class: " + className);
		System.out.println("Function: " + function.getDescription());
		outNames=function.outNames();
		paramNames=function.inNames();
		params=new String[paramNames.length];
		outputs=new String[outNames.length];
		referenced=new boolean[paramNames.length];
	}
	
	
	//IMPORTANTE!!!!!
	//Cuando se agrega una nueva funcion encadenable se debe agregar en este metodo
	
	/**
	 * Metodo que retorna la clase encadenable dado su nombre
	 * @param className El nombre canonico de la clase para la funcion que se va a ejecutar
	 * @return una funcion encadenable
	 */
	public IChainableArchiFunction classHandler(String className){
		if(className.equals("co.edu.uniandes.archimate.analysis.chain.catalog.Catalog")){
			name="Catalog";
			return new Catalog();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.catalog.CatalogType")){
			name="Catalog Type";
			return new CatalogType();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.catalog.CatalogAttribute")){
			name="Catalog Attribute";
			return new CatalogAttribute();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.catalog.CatalogTypeAttribute")){
			name="Catalog Type Attribute";
			return new CatalogTypeAttribute();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.matrix.Matrix")){
			name="Matrix";
			return new Matrix();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.countMatrix.CountMatrix")){
			name="Count Matrix";
			return new CountMatrix();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.getProperty.GetProperty")){
			name="Get Property";
			return new GetPropertyInRelation();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.getPropertyInRelation.GetPropertyInRelation")){
			name="Get Property";
			return new GetPropertyInRelation();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.getCatalogElement.GetCatalogElement")){
			name="Get Catalog Element";
			return new GetCatalogElement();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.addition.Addition")){
			name="Addition";
			return new Addition();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.childrenCatalog.ChildrenCatalog")){
			name="Children Catalog";
			return new ChildrenCatalog();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.relationshipsFromElement.RelationshipsFromElement")){
			name="Relationships from Catalog";
			return new RelationshipsFromElement();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.functional.process.processResponsibilityAssignment.ProcessResponsibilityAssignment")){
			name="Process responsibility assignment";
			return new ProcessResponsibilityAssignment();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.model.FilterModel")){
			name="Filter Model";
			return new FilterModel();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.model.ModelInput")){
			name="Model Input";
			return new ModelInput();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.functional.hr.raci.RaciMatrix")){
			name="RACI Matrix";
			return new DecompositionFunctional();
		}
		else{
			return null;
		}
	}

	public void setParams(ArrayList<String> inputs) {
		params=new String[inputs.size()];
		for(int i=0;i<inputs.size();i++){
			params[i]=inputs.get(i);
		}
	}

	public void setOutputs(String[] outputs) {
		this.outputs = outputs;
	}
	
	public String[] execute() throws Exception{
		outputs=function.executeFunction(params);
		return outputs;
	}

	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}



	public IChainableArchiFunction getFunction() {
		return function;
	}



	public String[] getParamNames() {
		return paramNames;
	}



	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}



	public String[] getOutNames() {
		return outNames;
	}



	public void setOutNames(String[] outNames) {
		this.outNames = outNames;
	}



	public boolean getReferenced(int i) {
		return referenced[i];
	}



	public void setReferenced(int i, boolean b) {
		referenced[i]=b;
	}



	public String[] getParams() {
		return params;
	}



	public String[] getOutputs() {
		return outputs;
	}
	
	
	
	
	
	
	
	
	
	

}
