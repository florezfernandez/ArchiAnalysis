package co.edu.uniandes.archimate.analysis.chain.utilities.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.archimate.analysis.ITableResult;

public class ResultSerializable implements Serializable {
	
	//----------------------------------------------
	//Attributes
	//----------------------------------------------
	private List<? extends ITableResult> resultEntries;
	
	private int[] columnSize;
	
	private String [] headers;

	//----------------------------------------------
	//Constructors
	//----------------------------------------------
	
	public ResultSerializable(List<? extends ITableResult> resultEntries,
			int[] columnSize, String[] headers) {
		super();
		this.resultEntries = resultEntries;
		this.columnSize = columnSize;
		this.headers = headers;
	}
	
	
	
	
	
	
	
	
	//---------------------------------------------
	//Getters and Setters
	//---------------------------------------------
	public List<? extends ITableResult> getResultEntries() {
		return resultEntries;
	}

	

	public void setResultEntries(List<? extends ITableResult> resultEntries) {
		this.resultEntries = resultEntries;
	}

	public int[] getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int[] columnSize) {
		this.columnSize = columnSize;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	
	

}
