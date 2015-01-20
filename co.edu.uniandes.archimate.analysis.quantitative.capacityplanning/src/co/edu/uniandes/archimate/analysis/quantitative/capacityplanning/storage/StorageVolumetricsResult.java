package co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.storage;

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.AnalysisPlugin;

import com.archimatetool.model.IArchimateElement;

public class StorageVolumetricsResult implements ITableResult{
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.##");

	public final static String LINE_DTL ="DETAIL";
	public final static String LINE_BLANK ="BLANK";
	public final static String LINE_LINE ="LINE";
	public final static String LINE_YEAR_HEADER ="YEAR_HEADER";
	public final static String LINE_YEAR_DETAIL ="YEAR_DETAIL";
	
	public IArchimateElement element;
	private String line;
	public Double txSize;
	public String fX;
	public Double vTx;
	public Double vTx1Year;
	public Double storageKB;
	public Double year1;
	public Double year2;
	public Double year3;
	public Double year4;
	public Double year5;
		
	
	public StorageVolumetricsResult(IArchimateElement element,String line)
	{
		this.element = element;
		this.line = line;
	}
	
	@Override
	public Object[] toArray(){
		if(this.line.equals(LINE_DTL))
			return new Object[]{element.getName(), DECIMAL_FORMAT.format(txSize), fX, DECIMAL_FORMAT.format(vTx), DECIMAL_FORMAT.format(vTx1Year), DECIMAL_FORMAT.format(storageKB)};
		if(this.line.equals(LINE_BLANK))
			return new Object[]{"", "", "", "", "", "" };
		if(this.line.equals(LINE_LINE))
			return new Object[]{"---", "---", "---", "---", "---", "---" };
		if(this.line.equals(LINE_YEAR_HEADER))
			return new Object[]{"", "1st Year (GB)", "2nd Year (GB)", "3rd Year (GB)", "4rd Year (GB)", "5th Year (GB)"};
		if(this.line.equals(LINE_YEAR_DETAIL))
			return new Object[]{element==null?"TOTAL":element.getName(), DECIMAL_FORMAT.format(year1), DECIMAL_FORMAT.format(year2), DECIMAL_FORMAT.format(year3), DECIMAL_FORMAT.format(year4), DECIMAL_FORMAT.format(year5) };

		return new Object[]{};
	}

	@Override
	public Image getImage(int index) {
		if(this.line.equals(LINE_YEAR_HEADER))
			if(index>0)
				return AbstractUIPlugin.imageDescriptorFromPlugin(AnalysisPlugin.PLUGIN_ID, "icons/menu-16.png").createImage();
		return null;
	}

	@Override
	public IArchimateElement getElement() {
		return element;
	}
}
