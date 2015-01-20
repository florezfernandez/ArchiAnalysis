package co.edu.uniandes.archimate.analysis.functional.correctness.datasecuritytransport;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.functional.correctness.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class DataSecurityTransportResult implements ITableResult{
	public static final String Statusr_Warning = "Warning";

	public IArchimateElement element;
	public String message;
	private String status;
	
	
	public DataSecurityTransportResult(String message, IArchimateElement element)
	{
		this.element = element;
		this.message = message;
		this.status = Statusr_Warning;
	}
	
	@Override
	public Object[] toArray(){
		return new Object[]{element.getName(), message, status };
	}

	@Override
	public Image getImage(int index) {
		if(index == 2)
			return AbstractUIPlugin.imageDescriptorFromPlugin(AnalysisPlugin.PLUGIN_ID, "icons/warning-16.png").createImage();
		return null;
	}

	@Override
	public IArchimateElement getElement() {
		return element;
	}

}
