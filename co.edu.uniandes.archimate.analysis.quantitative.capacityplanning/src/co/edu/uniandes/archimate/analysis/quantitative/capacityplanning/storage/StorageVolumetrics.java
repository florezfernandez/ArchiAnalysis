package co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.storage;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.Utils;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.WApplicationComponent;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.WApplicationInterface;
import co.edu.uniandes.archimate.analysis.quantitative.capacityplanning.WUsedByRelationship;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.ApplicationComponent;
import com.archimatetool.model.impl.ApplicationInterface;
import com.archimatetool.model.impl.CompositionRelationship;
import com.archimatetool.model.impl.UsedByRelationship;

public class StorageVolumetrics extends AbstractArchiAnalysisFunction{

	private List<StorageVolumetricsResult> aStorageResult;
	private List<WApplicationComponent> wApplicationComponents;
	private Double linearYearGrowth = 0d; 

	@Override
	public String getName(){return "Estimate overall solution architecture storage volumetric";}
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{

		linearYearGrowth = DiagramModelUtil.getDoubleValue(this.getDiagramModel(), "linearYearGrowth");
		if(linearYearGrowth==null) validationResponse.addError("The view level property Linear Year Growth % (linearYearGrowth) is not set or has a wrong value.",null);

		wApplicationComponents = new ArrayList<WApplicationComponent>();
		//get all component entities that contain the required property e: Internal or External component..
		@SuppressWarnings("unchecked")
		List<IArchimateElement> listComponents = getElements(ApplicationComponent.class);
		if(listComponents.size()>0)
		{
			for (IArchimateElement component : listComponents) {
				WApplicationComponent wApplicationComponent = new WApplicationComponent((ApplicationComponent)component);
				//wApplicationComponent.setE(DiagramModelUtil.getValue(component, "e"));
				//if(!wApplicationComponent.isValidE()) validationResponse.addError("Application Component Internal or External (e) has a wrong value. The valid values are: " + wApplicationComponent.getEValidValues() ,component);
				//get application interfaces
				List<IArchimateElement> listInterfaces = getTargetElementsByRelation(ApplicationInterface.class, CompositionRelationship.class, component);
				for (IArchimateElement interfaze : listInterfaces) {
					WApplicationInterface wApplicationInterface = new WApplicationInterface((ApplicationInterface)interfaze);

					wApplicationInterface.setTxSize(DiagramModelUtil.getDoubleValue(interfaze, "txSize"));
					if(wApplicationInterface.getTxSize()==null) validationResponse.addError("Application Interface Transaction size in bytes (txSize) is not set or has a wrong value.",interfaze);

					//wApplicationInterface.setMaxTt(DiagramModelUtil.getDoubleValue(interfaze, "maxTt"));
					//if(wApplicationInterface.getMaxTt()==null) validationResponse.addError("Application Interface maximum transfer time in seconds (maxTt) is not set or has a wrong value.",interfaze);

					//get all used by relationships
					List<IRelationship> listUsedByRelationship = getRelationsByRelationTypeAndSourceElement(UsedByRelationship.class, interfaze);
					for (IRelationship usedByRelationship : listUsedByRelationship) {
						WUsedByRelationship wUsedByRelationship = new WUsedByRelationship((UsedByRelationship)usedByRelationship);

						wUsedByRelationship.setFx(DiagramModelUtil.getValue(usedByRelationship, "fx"));
						if(!wUsedByRelationship.isValidFx()) validationResponse.addError("Used By Relation frequency (fx) has a wrong value. The valid values are: " + wUsedByRelationship.getFxValidValues() ,usedByRelationship);

						wUsedByRelationship.setVTx(DiagramModelUtil.getDoubleValue(usedByRelationship, "vTx"));
						if(wUsedByRelationship.getVTx()==null) validationResponse.addError("Used By Relation estimated volume transactions (vTx) is not set or has a wrong value.",usedByRelationship);

						//wUsedByRelationship.setCTx(DiagramModelUtil.getDoubleValue(usedByRelationship, "cTx"));
						//if(wUsedByRelationship.getCTx()==null) validationResponse.addError("Used By Relation estimated concurrent transactions (cTx) is not set or has a wrong value.",usedByRelationship);


						//get  business role targeted from relationships
						IArchimateElement component2 = getTargetElementFromRelationsByElement(ApplicationComponent.class,usedByRelationship);
						if(component2 == null)
							validationResponse.addError("Used By Relationship sourced by an application interface must have as target a application component.",usedByRelationship);
						else 
						{
							WApplicationComponent wApplicationComponent2 = new WApplicationComponent((ApplicationComponent)component2);
							//wApplicationComponent2.setE(DiagramModelUtil.getValue(component2, "e"));
							//if(!wApplicationComponent2.isValidE()) validationResponse.addError("Application Component Internal or External (e) has a wrong value. The valid values are: " + wApplicationComponent2.getEValidValues() ,component2);
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
		this.aStorageResult = new ArrayList<StorageVolumetricsResult>();
		StorageVolumetricsResult  blank = new StorageVolumetricsResult(null, StorageVolumetricsResult.LINE_BLANK);
		StorageVolumetricsResult  line = new StorageVolumetricsResult(null, StorageVolumetricsResult.LINE_LINE);
		StorageVolumetricsResult  yearHeaders = new StorageVolumetricsResult(null, StorageVolumetricsResult.LINE_YEAR_HEADER);
		StorageVolumetricsResult  yearTotals = new StorageVolumetricsResult(null, StorageVolumetricsResult.LINE_YEAR_DETAIL);
		yearTotals.year1 = 0d;

		for (WApplicationComponent wApplicationComponent : wApplicationComponents) {
			StorageVolumetricsResult  componentYearTotals = new StorageVolumetricsResult(wApplicationComponent.getElement(), StorageVolumetricsResult.LINE_YEAR_DETAIL);
			componentYearTotals.year1 = 0d;

			for (WApplicationInterface wApplicationInterface : wApplicationComponent.getWApplicationInterfaces()) {
				for (WUsedByRelationship wUsedByRelationship : wApplicationInterface.getWUsedByRelationships()) {
					@SuppressWarnings("unused")
					WApplicationComponent wApplicationComponent2 = wUsedByRelationship.getWApplicationComponent();
					StorageVolumetricsResult  result = new StorageVolumetricsResult(wUsedByRelationship.getElement(), StorageVolumetricsResult.LINE_DTL);

					result.fX =  wUsedByRelationship.getFx();
					result.txSize = wApplicationInterface.getTxSize();
					result.vTx = wUsedByRelationship.getVTx();
					result.vTx1Year = Utils.toYears(result.vTx, result.fX);
					result.storageKB = result.vTx1Year*result.txSize / 1024;
					componentYearTotals.year1+= result.storageKB;

					this.aStorageResult.add(result);
				}
			}
			componentYearTotals.year1 = componentYearTotals.year1/1024/1024;
			componentYearTotals.year2 = componentYearTotals.year1 *(100+linearYearGrowth)/100;
			componentYearTotals.year3 = componentYearTotals.year2 *(100+linearYearGrowth)/100; 
			componentYearTotals.year4 = componentYearTotals.year3 *(100+linearYearGrowth)/100; 
			componentYearTotals.year5 = componentYearTotals.year4 *(100+linearYearGrowth)/100;
			this.aStorageResult.add(blank);
			this.aStorageResult.add(yearHeaders);
			this.aStorageResult.add(componentYearTotals);
			yearTotals.year1 += componentYearTotals.year1;
		}

		yearTotals.year1 = yearTotals.year1;
		yearTotals.year2 = yearTotals.year1 *(100+linearYearGrowth)/100; 
		yearTotals.year3 = yearTotals.year2 *(100+linearYearGrowth)/100; 
		yearTotals.year4 = yearTotals.year3 *(100+linearYearGrowth)/100; 
		yearTotals.year5 = yearTotals.year4 *(100+linearYearGrowth)/100; 

		this.aStorageResult.add(blank);
		this.aStorageResult.add(line);
		this.aStorageResult.add(yearHeaders);
		this.aStorageResult.add(yearTotals);


		return null;
	}

	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"Element","TxSize (bytes)","fx: Frequency","vTx: Volume Transactions", "vTx 1Year","storage KB"};
		int[] widths = new int[]{200,150,150,150,150,150};
		createTable(headers, widths, this.aStorageResult, true, true );
		return null;
	}

	@Override
	public Boolean clearResults() {
		return true;
	}




}
