package co.edu.uniandes.archimate.analysis;

import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;


public interface IArchiAnalysisFunction  {

	public Object executeFunction () throws Exception;
	
	public Object validateModel (ValidationResponse validationResponse) throws Exception;

	public String getName();
	
	public Object showResults () throws Exception;
	
	public Boolean clearResults();
}
