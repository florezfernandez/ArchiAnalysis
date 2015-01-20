package co.edu.uniandes.archimate.analysis.quantitative.hr.workload;

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.quantitative.hr.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;

public class WorkloadResult implements ITableResult{
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

	public static final String Overwork = "Overwork";
	public static final String Appropriate = "Appropriate";
	public static final String Review = "Review";
	public static final String Idle = "Idle";
	
	public Double f;
	public Double t;
	public Double l;
	public Double n;
	public Double i;
	public Double c;
	public Double ns;
	public Double bt;
	public Double it;
	public Double ir;
	public Double cr;
	public Double y;
	public String desc;
	public String process;
	public String subprocess;
	public String role;
	public IRelationship assignment;

	@Override
	public Object[] toArray(){
		return new Object[]{process,
				subprocess,
				role,
				DECIMAL_FORMAT.format(f),
				DECIMAL_FORMAT.format(t),
				DECIMAL_FORMAT.format(l),
				DECIMAL_FORMAT.format(ns),
				DECIMAL_FORMAT.format(bt),
				DECIMAL_FORMAT.format(n),
				DECIMAL_FORMAT.format(i),
				DECIMAL_FORMAT.format(c),
				DECIMAL_FORMAT.format(it),
				DECIMAL_FORMAT.format(ir),
				DECIMAL_FORMAT.format(cr),
				DECIMAL_FORMAT.format(y),
				desc};
	}

	@Override
	public Image getImage(int index) {
		if(index == 15)
			return AbstractUIPlugin.imageDescriptorFromPlugin(AnalysisPlugin.PLUGIN_ID, "icons/"+desc+"-16.png").createImage();
		return null;
	}

	@Override
	public IArchimateElement getElement() {
		return assignment;
	}

}
