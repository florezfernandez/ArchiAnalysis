package co.edu.uniandes.archimate.analysis.chain.utilities;


import java.util.ArrayList;

import sun.security.util.Length;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.catalog.Catalog;
import co.edu.uniandes.archimate.analysis.chain.countMatrix.CountMatrix;
import co.edu.uniandes.archimate.analysis.chain.matrix.Matrix;

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
		outNames=function.outNames();
		paramNames=function.inNames();
		params=new String[paramNames.length];
		outputs=new String[outNames.length];
		referenced=new boolean[paramNames.length];
	}
	
	public IChainableArchiFunction classHandler(String className){
		if(className.equals("co.edu.uniandes.archimate.analysis.chain.catalog.Catalog")){
			name="Catalog";
			return new Catalog();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.matrix.Matrix")){
			name="Matrix";
			return new Matrix();
		}
		else if(className.equals("co.edu.uniandes.archimate.analysis.chain.countMatrix.CountMatrix")){
			name="Count Matrix";
			return new CountMatrix();
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