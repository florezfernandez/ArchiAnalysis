package co.edu.uniandes.archimate.analysis.functional.correctness.datasecuritytransport;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.impl.ApplicationInterface;

public class WApplicationInterface extends WAbstractSDTElement{

	public final static String STP_SSH = "SSH";
	public final static String STP_SSL = "SSL";
	public final static String STP_TLS = "TLS";
	public final static String STP_NONE ="NONE";

	public final static String UF_EXTERNAL = "EXTERNAL";
	public final static String UF_INTERNAL = "INTERNAL";

	private ApplicationInterface element;
	private List<WUsedByRelationship> wUsedByRelationships;	
	private String p;
	private String e;

	public ApplicationInterface getElement() {
		return element;
	}

	public List<WUsedByRelationship> getWUsedByRelationships() {
		return wUsedByRelationships;
	}
	public void addWUsedByRelationships(WUsedByRelationship wUsedByRelationship) {
		this.wUsedByRelationships.add(wUsedByRelationship);
	}

	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p!=null? p.toUpperCase():p;
	}
	public boolean isValidP()
	{
		return ((this.p!=null) && (p.equals(STP_SSH) || p.equals(STP_SSL) || p.equals(STP_TLS) || p.equals(STP_NONE)));
	}
	public String getPValidValues ()
	{
		return STP_SSH+", "+ STP_SSL+", "+ STP_TLS+", "+ STP_NONE;
	}


	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e!=null? e.toUpperCase():e;
	}
	public boolean isValidE()
	{
		return ((this.e!=null) && (e.equals(UF_EXTERNAL) || e.equals(UF_INTERNAL)));
	}
	public String getEValidValues ()
	{
		return UF_EXTERNAL+", "+ UF_INTERNAL;
	}


	public WApplicationInterface(ApplicationInterface element) {
		this.element = element;
		this.wUsedByRelationships = new ArrayList<WUsedByRelationship>();
	}


}
