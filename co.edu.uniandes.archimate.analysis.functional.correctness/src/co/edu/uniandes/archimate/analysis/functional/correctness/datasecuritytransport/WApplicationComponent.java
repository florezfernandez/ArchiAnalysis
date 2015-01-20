package co.edu.uniandes.archimate.analysis.functional.correctness.datasecuritytransport;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.impl.ApplicationComponent;

public class WApplicationComponent extends WAbstractSDTElement{
	
	public final static String UF_EXTERNAL = "EXTERNAL";
	public final static String UF_INTERNAL = "INTERNAL";

	private ApplicationComponent element;
	private List<WApplicationInterface> wApplicationInterfaces;	

	private String e;
	
	public ApplicationComponent getElement() {
		return element;
	}
	
	public List<WApplicationInterface> getWApplicationInterfaces() {
		return wApplicationInterfaces;
	}
	public void addWApplicationInterface(WApplicationInterface wApplicationInterface) {
		this.wApplicationInterfaces.add(wApplicationInterface);
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
	
	
	public WApplicationComponent(ApplicationComponent element) {
		this.element = element;
		this.wApplicationInterfaces = new ArrayList<WApplicationInterface>();
	}
	
}
