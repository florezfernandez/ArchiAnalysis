package co.edu.uniandes.archimate.analysis.quantitative.capacityplanning;

import com.archimatetool.model.impl.UsedByRelationship;


public class WUsedByRelationship extends WAbstractSDTElement{

	public final static String FREQ_DAILY = "DAILY";
	public final static String FREQ_WEEKLY = "WEEKLY";
	public final static String FREQ_MONTHLY = "MONTHLY";
	public final static String FREQ_QUARTERLY = "QUARTERLY";

	private UsedByRelationship element;
	private String fX;
	private Double vTx;
	private Double cTx;
	private WApplicationComponent wApplicationComponent;

	public UsedByRelationship getElement() {
		return element;
	}

	public String getFx() {
		return fX;
	}
	public void setFx(String fX) {
		this.fX = fX!=null? fX.toUpperCase().trim():fX;
	}
	public boolean isValidFx()
	{
		return ((this.fX!=null) && (fX.equals(FREQ_DAILY) || fX.equals(FREQ_WEEKLY) || fX.equals(FREQ_MONTHLY) || fX.equals(FREQ_QUARTERLY)));
	}
	public String getFxValidValues ()
	{
		return FREQ_DAILY+", "+ FREQ_WEEKLY+", "+ FREQ_MONTHLY+", "+ FREQ_QUARTERLY;
	}

	public Double getVTx() {
		return vTx;
	}	
	public void setVTx(Double vTx) {
		this.vTx = vTx;
	}

	public Double getCTx() {
		return cTx;
	}	
	public void setCTx(Double cTx) {
		this.cTx = cTx;
	}

	public WApplicationComponent getWApplicationComponent() {
		return wApplicationComponent;
	}
	public void setWApplicationComponent(WApplicationComponent wApplicationComponent) {
		this.wApplicationComponent = wApplicationComponent;
	}

	public WUsedByRelationship(UsedByRelationship element) {
		this.element = element;
	}
}
