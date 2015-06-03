package co.edu.uniandes.archimate.analysis.analysischain;


public interface IChainableArchiFunction{
	
	//-------------------------
	//
	//-------------------------
	
	public String[] displayWidget();
	
	public String getDescription();
	
	public String[] executeFunction (String[] params) throws Exception;
	
	public String[] inNames();
	
	public String[] outNames();
	
	
	

}
