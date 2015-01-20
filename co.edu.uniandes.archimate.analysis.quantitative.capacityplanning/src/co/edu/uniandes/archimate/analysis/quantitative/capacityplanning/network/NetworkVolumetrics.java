package co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.network;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.AnalysisProperty;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedElement;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.Utils;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.WApplicationComponent;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.WApplicationInterface;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.WUsedByRelationship;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.storage.StorageVolumetricsResult;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.ApplicationComponent;
import com.archimatetool.model.impl.ApplicationInterface;
import com.archimatetool.model.impl.CompositionRelationship;
import com.archimatetool.model.impl.UsedByRelationship;

public class NetworkVolumetrics extends AbstractArchiAnalysisFunction{

	private List<NetworkVolumetricsResult> aNetworkResult;
	private List<WApplicationComponent> wApplicationComponents;
	private Double linearYearGrowth = 0d; 

	@Override
	public String getName(){return "Estimate overall solution architecture network volumetrics";}
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{

		linearYearGrowth = DiagramModelUtil.getDoubleValue(this.getDiagramModel(), "linearYearGrowth");
		if(linearYearGrowth==null) validationResponse.addError("The view level property Linear Year Growth % (linearYearGrowth) is not set or has a wrong value.",null);

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

					wApplicationInterface.setTxSize(DiagramModelUtil.getDoubleValue(interfaze, "txSize"));
					if(wApplicationInterface.getTxSize()==null) validationResponse.addError("Application Interface Transaction size in bytes (txSize) is not set or has a wrong value.",interfaze);

					wApplicationInterface.setMaxTt(DiagramModelUtil.getDoubleValue(interfaze, "maxTt"));
					if(wApplicationInterface.getMaxTt()==null) validationResponse.addError("Application Interface maximum transfer time in seconds (maxTt) is not set or has a wrong value.",interfaze);

					//get all used by relationships
					List<IRelationship> listUsedByRelationship = getRelationsByRelationTypeAndSourceElement(UsedByRelationship.class, interfaze);
					for (IRelationship usedByRelationship : listUsedByRelationship) {
						WUsedByRelationship wUsedByRelationship = new WUsedByRelationship((UsedByRelationship)usedByRelationship);

						wUsedByRelationship.setFx(DiagramModelUtil.getValue(usedByRelationship, "fx"));
						if(!wUsedByRelationship.isValidFx()) validationResponse.addError("Used By Relation frequency (fx) has a wrong value. The valid values are: " + wUsedByRelationship.getFxValidValues() ,usedByRelationship);

						//wUsedByRelationship.setVTx(DiagramModelUtil.getDoubleValue(usedByRelationship, "vTx"));
						//if(wUsedByRelationship.getVTx()==null) validationResponse.addError("Used By Relation estimated volume transactions (vTx) is not set or has a wrong value.",usedByRelationship);

						wUsedByRelationship.setCTx(DiagramModelUtil.getDoubleValue(usedByRelationship, "cTx"));
						if(wUsedByRelationship.getCTx()==null) validationResponse.addError("Used By Relation estimated concurrent transactions (cTx) is not set or has a wrong value.",usedByRelationship);


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
				if(wApplicationComponent.getWApplicationInterfaces().size()>0)
					wApplicationComponents.add(wApplicationComponent);
			}

		}else{
			validationResponse.addError("No any component element in the model has the required property e: Internal or External Component.");
		}

		return null;
	}	

	@Override
	public Object executeFunction() throws Exception {
		this.aNetworkResult = new ArrayList<NetworkVolumetricsResult>();
		NetworkVolumetricsResult  blank = new NetworkVolumetricsResult(null, StorageVolumetricsResult.LINE_BLANK);
		NetworkVolumetricsResult  line = new NetworkVolumetricsResult(null, StorageVolumetricsResult.LINE_LINE);
		NetworkVolumetricsResult  yearHeaders = new NetworkVolumetricsResult(null, StorageVolumetricsResult.LINE_YEAR_HEADER);
		NetworkVolumetricsResult  yearTotalsPrivate = new NetworkVolumetricsResult(null, StorageVolumetricsResult.LINE_YEAR_DETAIL);
		NetworkVolumetricsResult  yearTotalsPublic = new NetworkVolumetricsResult(null, StorageVolumetricsResult.LINE_YEAR_DETAIL);
		yearTotalsPrivate.year1 = 0d;
		yearTotalsPublic.year1 = 0d;


		for (WApplicationComponent wApplicationComponent : wApplicationComponents) {
			NetworkVolumetricsResult  componentYearTotalsPrivate = new NetworkVolumetricsResult(wApplicationComponent.getElement(), StorageVolumetricsResult.LINE_YEAR_DETAIL);
			NetworkVolumetricsResult  componentYearTotalsPublic = new NetworkVolumetricsResult(wApplicationComponent.getElement(), StorageVolumetricsResult.LINE_YEAR_DETAIL);
			componentYearTotalsPrivate.year1 = 0d;
			componentYearTotalsPublic.year1 = 0d;
			for (WApplicationInterface wApplicationInterface : wApplicationComponent.getWApplicationInterfaces()) {
				for (WUsedByRelationship wUsedByRelationship : wApplicationInterface.getWUsedByRelationships()) {
					WApplicationComponent wApplicationComponent2 = wUsedByRelationship.getWApplicationComponent();
					NetworkVolumetricsResult  result = new NetworkVolumetricsResult(wUsedByRelationship.getElement(), StorageVolumetricsResult.LINE_DTL);

					result.txSize = wApplicationInterface.getTxSize();
					result.maxTt = wApplicationInterface.getMaxTt();
					result.cTx  = wUsedByRelationship.getCTx();
					result.networkKbps = result.cTx * result.txSize / Utils.STORAGE_1KB_TO_Bytes * Utils.STORAGE_1KB_TO_BITS / Utils.NETWORK_1KBPs_TO_bps / result.maxTt;
					result.networkMbps = result.networkKbps / Utils.NETWORK_1KBPs_TO_bps;
					result.e = wApplicationComponent2.getE();

					if(result.e.equals(NetworkVolumetricsResult.EXTERNAL))
						componentYearTotalsPublic.year1= componentYearTotalsPublic.year1< result.networkMbps? result.networkMbps: componentYearTotalsPublic.year1;
					if(result.e.equals(NetworkVolumetricsResult.INTERNAL))
						componentYearTotalsPrivate.year1= componentYearTotalsPrivate.year1< result.networkMbps? result.networkMbps: componentYearTotalsPrivate.year1;
 
					this.aNetworkResult.add(result);

				}
			}
			componentYearTotalsPublic.year1 = componentYearTotalsPublic.year1;
			componentYearTotalsPublic.year2 = componentYearTotalsPublic.year1 *(100+linearYearGrowth)/100;
			componentYearTotalsPublic.year3 = componentYearTotalsPublic.year2 *(100+linearYearGrowth)/100; 
			componentYearTotalsPublic.year4 = componentYearTotalsPublic.year3 *(100+linearYearGrowth)/100; 
			componentYearTotalsPublic.year5 = componentYearTotalsPublic.year4 *(100+linearYearGrowth)/100;
			componentYearTotalsPublic.e = NetworkVolumetricsResult.EXTERNAL;
			
			componentYearTotalsPrivate.year1 = componentYearTotalsPrivate.year1;
			componentYearTotalsPrivate.year2 = componentYearTotalsPrivate.year1 *(100+linearYearGrowth)/100;
			componentYearTotalsPrivate.year3 = componentYearTotalsPrivate.year2 *(100+linearYearGrowth)/100; 
			componentYearTotalsPrivate.year4 = componentYearTotalsPrivate.year3 *(100+linearYearGrowth)/100; 
			componentYearTotalsPrivate.year5 = componentYearTotalsPrivate.year4 *(100+linearYearGrowth)/100;
			componentYearTotalsPrivate.e = NetworkVolumetricsResult.INTERNAL;

			this.aNetworkResult.add(blank);
			this.aNetworkResult.add(yearHeaders);
			this.aNetworkResult.add(componentYearTotalsPublic);
			this.aNetworkResult.add(componentYearTotalsPrivate);
			yearTotalsPublic.year1 += componentYearTotalsPublic.year1;
			yearTotalsPrivate.year1 += componentYearTotalsPrivate.year1;
		}


		yearTotalsPublic.year1 = yearTotalsPublic.year1;
		yearTotalsPublic.year2 = yearTotalsPublic.year1 *(100+linearYearGrowth)/100; 
		yearTotalsPublic.year3 = yearTotalsPublic.year2 *(100+linearYearGrowth)/100; 
		yearTotalsPublic.year4 = yearTotalsPublic.year3 *(100+linearYearGrowth)/100; 
		yearTotalsPublic.year5 = yearTotalsPublic.year4 *(100+linearYearGrowth)/100;
		yearTotalsPublic.e = NetworkVolumetricsResult.EXTERNAL;

		yearTotalsPrivate.year1 = yearTotalsPrivate.year1;
		yearTotalsPrivate.year2 = yearTotalsPrivate.year1 *(100+linearYearGrowth)/100; 
		yearTotalsPrivate.year3 = yearTotalsPrivate.year2 *(100+linearYearGrowth)/100; 
		yearTotalsPrivate.year4 = yearTotalsPrivate.year3 *(100+linearYearGrowth)/100; 
		yearTotalsPrivate.year5 = yearTotalsPrivate.year4 *(100+linearYearGrowth)/100; 
		yearTotalsPrivate.e = NetworkVolumetricsResult.INTERNAL;

		
		this.aNetworkResult.add(blank);
		this.aNetworkResult.add(line);
		this.aNetworkResult.add(yearHeaders);
		this.aNetworkResult.add(yearTotalsPublic);
		this.aNetworkResult.add(yearTotalsPrivate);
		
		return null;
	}

	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"Element","TxSize (bytes)","MaxTt (sec)", "cTx: concurrent Tx", "network Kbps","network Mbps"};
		int[] widths = new int[]{200,150,150,150,150,150};
		createTable(headers, widths, this.aNetworkResult, true, true );
		return null;
	}

	@Override
	public Boolean clearResults() {
		return true;
	}




}
