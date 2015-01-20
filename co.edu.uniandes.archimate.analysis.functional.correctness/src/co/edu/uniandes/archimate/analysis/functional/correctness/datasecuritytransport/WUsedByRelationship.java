package co.edu.uniandes.archimate.analysis.functional.correctness.datasecuritytransport;

import com.archimatetool.model.impl.UsedByRelationship;

public class WUsedByRelationship extends WAbstractSDTElement{

	public final static String TSD_YES = "YES";
	public final static String TSD_NO = "NO";
	
	public final static String UDP_MSGE = "MESSAGE ENCRYPTION";
	public final static String UDP_FLDE = "FIELD ENCRYPTION";
	public final static String UDP_FLDO = "FIELD OBFUSCATION";
	public final static String UDP_NONE = "NONE";
	
	private UsedByRelationship element;
	private String s;
	private String u;
	private WApplicationComponent wApplicationComponent;
	
	public UsedByRelationship getElement() {
		return element;
	}
	
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s!=null? s.toUpperCase():s;
	}
	public boolean isValidS()
	{
		return ((this.s!=null) && (s.equals(TSD_YES) || s.equals(TSD_NO)));
	}
	public String getSValidValues ()
	{
		return TSD_YES+", "+ TSD_NO;
	}
	
	public String getU() {
		return u;
	}	
	public void setU(String u) {
		this.u = u!=null? u.toUpperCase():u;
	}
	public boolean isValidU()
	{
		return ((this.u!=null) && (u.equals(UDP_MSGE) || u.equals(UDP_FLDE) || u.equals(UDP_FLDO) || u.equals(UDP_NONE)));
	}
	public String getUValidValues ()
	{
		return UDP_MSGE+", "+ UDP_FLDE+", "+ UDP_FLDO+", "+ UDP_NONE;
	}
	
	public WApplicationComponent getWApplicationComponent() {
		return wApplicationComponent;
	}
	public void setWApplicationComponent(
			WApplicationComponent wApplicationComponent) {
		this.wApplicationComponent = wApplicationComponent;
	}
	
	public WUsedByRelationship(UsedByRelationship element) {
		this.element = element;
	}
}
