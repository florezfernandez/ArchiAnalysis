package co.edu.uniandes.archimate.analysis.functional.correctness.datasecuritytransport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.AnalysisProperty;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedElement;
import co.edu.uniandes.archimate.analysis.util.ColorSheme;
import co.edu.uniandes.archimate.analysis.util.ColorUtil;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;
import co.edu.uniandes.archimate.analysis.util.GEFUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.ApplicationComponent;
import com.archimatetool.model.impl.ApplicationInterface;
import com.archimatetool.model.impl.CompositionRelationship;
import com.archimatetool.model.impl.UsedByRelationship;

public class DataSecurityTransport extends AbstractArchiAnalysisFunction{

	private List<DataSecurityTransportResult> aDataSecurityTransportResult;
	private List<WApplicationComponent> wApplicationComponents;
	private Map<IArchimateElement, ColorSheme[]> originalColor = new HashMap<IArchimateElement, ColorSheme[]>();

	@Override
	public String getName(){return "Data security compliance at transport level";}
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		wApplicationComponents = new ArrayList<WApplicationComponent>();
		//get all component entities that contain the required property e: Internal or External component..
		List<IArchimateElement> listComponents = getWeakTypedElements(new WeakTypedElement(ApplicationComponent.class, new AnalysisProperty("e",DiagramModelUtil.DEFFAUL_VALUE)));
		if(listComponents.size()>0)
		{
			for (IArchimateElement component : listComponents) {
				WApplicationComponent wApplicationComponent = new WApplicationComponent((ApplicationComponent)component);
				wApplicationComponent.setE(DiagramModelUtil.getValue(component, "e"));
				if(!wApplicationComponent.isValidE()) validationResponse.addError("Application Component Internal or External (e) has a wrong value. The valid values are: " + wApplicationComponent.getEValidValues() ,component);
				//get application interfaces
				List<IArchimateElement> listInterfaces = getTargetElementsByRelation(ApplicationInterface.class, CompositionRelationship.class, component);
				for (IArchimateElement interfaze : listInterfaces) {
					WApplicationInterface wApplicationInterface = new WApplicationInterface((ApplicationInterface)interfaze);

					wApplicationInterface.setE(DiagramModelUtil.getValue(interfaze, "e"));
					if(!wApplicationInterface.isValidE()) validationResponse.addError("Application Interface Internal or External (e) has a wrong value. The valid values are: " + wApplicationInterface.getEValidValues() ,interfaze);

					wApplicationInterface.setP(DiagramModelUtil.getValue(interfaze, "p"));
					if(!wApplicationInterface.isValidP()) validationResponse.addError("Application Interface Secure Transport Protocol (p) has a wrong value. The valid values are: " + wApplicationInterface.getPValidValues() ,interfaze);

					//get all used by relationships
					List<IRelationship> listUsedByRelationship = getRelationsByRelationTypeAndSourceElement(UsedByRelationship.class, interfaze);
					for (IRelationship usedByRelationship : listUsedByRelationship) {
						WUsedByRelationship wUsedByRelationship = new WUsedByRelationship((UsedByRelationship)usedByRelationship);

						wUsedByRelationship.setS(DiagramModelUtil.getValue(usedByRelationship, "s"));
						if(!wUsedByRelationship.isValidS()) validationResponse.addError("Used By Relation Transpor Security Data (s) has a wrong value. The valid values are: " + wUsedByRelationship.getSValidValues() ,usedByRelationship);

						wUsedByRelationship.setU(DiagramModelUtil.getValue(usedByRelationship, "u"));
						if(!wUsedByRelationship.isValidU()) validationResponse.addError("Used By Relation Uses Data Protection (u) has a wrong value. The valid values are: " + wUsedByRelationship.getUValidValues() ,usedByRelationship);

						//get  business role targeted from relationships
						IArchimateElement component2 = getTargetElementFromRelationsByElement(ApplicationComponent.class,usedByRelationship);
						if(component2 == null)
							validationResponse.addError("Used By Relationship sourced by an application interface must have as target a application component.",usedByRelationship);
						else 
						{
							WApplicationComponent wApplicationComponent2 = new WApplicationComponent((ApplicationComponent)component2);
							wApplicationComponent2.setE(DiagramModelUtil.getValue(component2, "e"));
							if(!wApplicationComponent2.isValidE()) validationResponse.addError("Application Component Internal or External (e) has a wrong value. The valid values are: " + wApplicationComponent2.getEValidValues() ,component2);
							wUsedByRelationship.setWApplicationComponent(wApplicationComponent2);
						}
						wApplicationInterface.addWUsedByRelationships(wUsedByRelationship);
					}
					wApplicationComponent.addWApplicationInterface(wApplicationInterface);
				}
				wApplicationComponents.add(wApplicationComponent);
			}

		}else{
			validationResponse.addError("No any component element in the model has the required property e: Internal or External Component.");
		}

		return null;
	}	

	@Override
	public Object executeFunction() throws Exception {
		this.aDataSecurityTransportResult = new ArrayList<DataSecurityTransportResult>();
		for (WApplicationComponent wApplicationComponent : wApplicationComponents) {
			for (WApplicationInterface wApplicationInterface : wApplicationComponent.getWApplicationInterfaces()) {

				//if an interface composes a external component the interface will be external too or if an interface 
				//composes a internal component the interface will be internal too.
				if(!wApplicationInterface.getE().equals(wApplicationComponent.getE()))
				{
					aDataSecurityTransportResult.add(new DataSecurityTransportResult("Internal/External components must contain only Internal/External interfaces respectively.", wApplicationComponent.getElement()));
					wApplicationComponent.setWarningStatus(true);
					aDataSecurityTransportResult.add(new DataSecurityTransportResult("Internal/External interfaces must be contained only by Internal/External interfaces respectively.", wApplicationInterface.getElement()));
					wApplicationInterface.setWarningStatus(true);
				}

				for (WUsedByRelationship wUsedByRelationship : wApplicationInterface.getWUsedByRelationships()) {
					WApplicationComponent wApplicationComponent2 = wUsedByRelationship.getWApplicationComponent();

					//if an external component uses an internal interface whose secure transport protocol is set to none.
					if(wApplicationInterface.getE().equals(WApplicationComponent.UF_INTERNAL) && wApplicationComponent2.getE().equals(WApplicationComponent.UF_EXTERNAL) && wApplicationInterface.getP().equals(WApplicationInterface.STP_NONE)){
						aDataSecurityTransportResult.add(new DataSecurityTransportResult("External components must use Internal interfaces using a secure transport protocol.", wUsedByRelationship.getElement()));						
						wUsedByRelationship.setWarningStatus(true);					
					}
					//if an internal component uses an external interface whose secure transport protocol is set to none.
					if(wApplicationInterface.getE().equals(WApplicationComponent.UF_EXTERNAL) && wApplicationComponent2.getE().equals(WApplicationComponent.UF_INTERNAL) && wApplicationInterface.getP().equals(WApplicationInterface.STP_NONE)){
						aDataSecurityTransportResult.add(new DataSecurityTransportResult("Internal components must use External interfaces using a secure transport protocol.", wUsedByRelationship.getElement()));						
						wUsedByRelationship.setWarningStatus(true);					
					}
					//if an internal component uses an internal interface whose secure transport protocol is set to none 
					//and the relation between them carried sensitive data but not uses any data protection.
					if(wApplicationInterface.getE().equals(WApplicationComponent.UF_INTERNAL) && wApplicationComponent2.getE().equals(WApplicationComponent.UF_INTERNAL) && wUsedByRelationship.getS().equals(WUsedByRelationship.TSD_YES) && wUsedByRelationship.getU().equals(WUsedByRelationship.UDP_NONE) && wApplicationInterface.getP().equals(WApplicationInterface.STP_NONE)){
						aDataSecurityTransportResult.add(new DataSecurityTransportResult("Internal components that uses internal interfaces that exposes sensitive data must use a secure transport protocol.", wUsedByRelationship.getElement()));						
						wUsedByRelationship.setWarningStatus(true);					
					}
					//if any interface is in a warning state all used by relations must be set to warning too.
					if(wApplicationInterface.isWarningStatus())
					{						
						aDataSecurityTransportResult.add(new DataSecurityTransportResult("If the interface used by the component is in warning status must be reviewed the relation too.", wUsedByRelationship.getElement()));						
						wUsedByRelationship.setWarningStatus(true);											
					}
				}
			}
		}

		return null;
	}

	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"Element","Message","Status"};
		int[] widths = new int[]{200,600,100};
		createTable(headers, widths, this.aDataSecurityTransportResult, true, true );
		// Model
		for (DataSecurityTransportResult result : aDataSecurityTransportResult) {
			String fillColor =  ColorUtil.Red;
			ColorSheme[] originalColorSheme = GEFUtil.changeColor(getADiagramEditor(), result.element, fillColor);
			if(!originalColor.containsKey(result.element))
				originalColor.put(result.element, originalColorSheme);
		}
		return null;
	}

	@Override
	public Boolean clearResults() {
		for(Iterator<Entry<IArchimateElement, ColorSheme[]>> iter = originalColor.entrySet().iterator(); iter.hasNext();) {
			Entry<IArchimateElement, ColorSheme[]> elementColor = iter.next();
			GEFUtil.changeColor(getADiagramEditor(), elementColor.getKey(), elementColor.getValue());
		}
		return true;
	}




}
