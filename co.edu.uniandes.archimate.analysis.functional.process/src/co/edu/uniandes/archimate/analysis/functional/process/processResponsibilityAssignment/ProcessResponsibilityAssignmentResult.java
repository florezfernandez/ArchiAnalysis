package co.edu.uniandes.archimate.analysis.functional.process.processResponsibilityAssignment;

import java.io.Serializable;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.functional.process.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class ProcessResponsibilityAssignmentResult implements ITableResult, Serializable{
	private String elementIdTarget;
	private String elementNameTarget;
	private String elementIdSource;
	private String elementNameSource;
	
	/**
	 * 
	 * @param elementType
	 * @param line
	 */
	public ProcessResponsibilityAssignmentResult(String elementIdTarget, String elementNameTarget, String elementIdSource, String elementNameSource){
		this.elementIdTarget = elementIdTarget;
		this.elementNameTarget = elementNameTarget;
		this.elementIdSource = elementIdSource;
		this.elementNameSource = elementNameSource;
	}
	
	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#toArray()
	 */
	@Override
	public Object[] toArray() {
		return new Object[]{elementIdTarget,elementNameTarget,elementIdSource,elementNameSource};
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.ITableResult#getImage(int)
	 */
	@Override
	public Image getImage(int index) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.IResult#getElement()
	 */
	@Override
	public IArchimateElement getElement() {
		return null;
	}
}
