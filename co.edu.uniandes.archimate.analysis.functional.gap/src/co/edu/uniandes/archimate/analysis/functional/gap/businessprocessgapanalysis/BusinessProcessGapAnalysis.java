package co.edu.uniandes.archimate.analysis.functional.gap.businessprocessgapanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class BusinessProcessGapAnalysis extends AbstractArchiAnalysisFunction{

	private List<BusinessProcessGapAnalysisResult> aBPGapAnalysisResult;
	private HashMap<String,WRelation> relationshipListASIS;
	private HashMap<String,WElement> elementListASIS;
	private HashMap<String,WRelation> relationshipListTOBE;
	private HashMap<String,WElement> elementListTOBE;
	@Override
	public String getName(){return "Gap Analysis";}
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		relationshipListASIS=new HashMap<String,WRelation>();
		elementListASIS=new HashMap<String,WElement>();
		relationshipListTOBE=new HashMap<String,WRelation>();
		elementListTOBE=new HashMap<String,WElement>();

		@SuppressWarnings("unchecked")
		List<IRelationship> relationships = getRelations(IRelationship.class);
		Set<EObject> elements = new HashSet<EObject>();

		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
			searchElementsRecursively(diagramModelObj, elements);
		}

		for(EObject eObject : elements){
			WElement wElement= new WElement(eObject);
			if(wElement.getModel()!=null){
				if(wElement.getModel().equals("AS-IS")){
					elementListASIS.put(wElement.getId(), wElement);
				}
				else if(wElement.getModel().equals("TO-BE")){
					elementListTOBE.put(wElement.getId(), wElement);
				}
				else{
					//BOTAR ERROR DE VALIDACION POR NO TENER MODELO BIEN PUESTO
				}
			}
			else{
				//ACA BOTAR ERROR DE VALIDACION POR TENER UN ELEMENTO SIN MODELO
			}
		}
		for(IRelationship relationship:relationships){
			WRelation wRelation = new WRelation(relationship);
			String idSource=wRelation.getIdSource();
			String idTarget=wRelation.getIdTarget();
			if(wRelation.getModel()!=null){
				if(wRelation.getModel().equals("AS-IS")){
					relationshipListASIS.put(wRelation.getId(), wRelation);
					elementListASIS.get(idSource).getIdTarget().put(idTarget, wRelation);
					elementListASIS.get(idTarget).getIdSource().put(idSource, wRelation);
					
				}
				else if(wRelation.getModel().equals("TO-BE")){
					relationshipListTOBE.put(wRelation.getId(), wRelation);
					elementListTOBE.get(idSource).getIdTarget().put(idTarget, wRelation);
					elementListTOBE.get(idTarget).getIdSource().put(idSource, wRelation);
				}
				else{
					//BOTAR ERROR DE VALIDACION POR NO TENER MODELO BIEN PUESTO
				}
			}
			else{
				//ACA BOTAR ERROR DE VALIDACION POR TENER UN ELEMENTO SIN MODELO
			}
			
		}
		
		//		
		//		List<IArchimateElement> listPlateu = getWeakTypedElements(new WeakTypedElement(Plateau.class, new AnalysisProperty("type",DiagramModelUtil.DEFFAUL_VALUE)));
		//		if(listPlateu.size()==2){
		//			for(IArchimateElement plateu:listPlateu){
		//				String model=DiagramModelUtil.getValue(plateu, "type");
		//				List<IArchimateElement> bigProcess = getTargetElementsByRelation(BusinessProcess.class, AggregationRelationship.class, plateu);
		//				IArchimateElement mainP=bigProcess.get(0);
		//				List<IArchimateElement> listElements = getTargetElementsByRelation(BusinessProcess.class, CompositionRelationship.class, mainP);
		//				for(IArchimateElement element:listElements){
		//					WElement wBP=new WElement((BusinessProcess) element);
		//					//ACA IRIA LO DEL TYPE
		//					String id=DiagramModelUtil.getValue(element, "id");
		//					List<IRelationship> listTriggeringRelationship = getRelationsByRelationTypeAndTargetElement(TriggeringRelationship.class, element);
		//					for(IRelationship relation: listTriggeringRelationship){
		//						IArchimateElement archiElement = relation.getSource();
		//						String idSource=DiagramModelUtil.getValue(archiElement, "id");
		//						wBP.getIdSource().put(idSource, 1);
		//						//Aca deberia poner lo del target.
		//					}
		//					
		//					
		//					
		//					
		//					
		//					if(model.equals("AS-IS")){
		//						//addElementsRecursively(element,beListASIS,bpListASIS,null);
		//						elementListASIS.put(id, wBP);
		//					}
		//					else if(model.equals("TO-BE")){
		//						elementListTOBE.put(id, wBP);
		//						//addElementsRecursively(element,beListTOBE,bpListTOBE,null);
		//					}
		//					
		//				}
		//			}
		//		}
		//		else{
		//			validationResponse.addError("There are not 2 plateus with property type");
		//		}
		return null;
	}	

	//	public String addElementsRecursively(IArchimateElement element, HashMap<String, WRelation> beList, HashMap<String, WElement> bpList,String source){ 
	//		String id=DiagramModelUtil.getValue(element, "id");
	//		
	//		if(element.getClass().equals(BusinessEvent.class)){
	//			WRelation wBE=new WRelation((BusinessEvent) element);
	//			wBE.setId(id);
	//			List<IArchimateElement> listElements = getTargetElementsByRelation(BusinessProcess.class, TriggeringRelationship.class, element);
	//			for(IArchimateElement nextElement:listElements){
	//				wBE.getIdTarget().put(addElementsRecursively(nextElement, beList, bpList,id), 1);
	//				if(source!=null){
	//					wBE.getIdSource().put(source, 1);
	//				}
	//			}
	//			List<IArchimateElement> listElements2 = getTargetElementsByRelation(BusinessEvent.class, TriggeringRelationship.class, element);
	//			for(IArchimateElement nextElement:listElements2){
	//				wBE.getIdTarget().put(addElementsRecursively(nextElement, beList, bpList,id), 1);
	//				if(source!=null){
	//					wBE.getIdSource().put(source, 1);
	//				}
	//			}
	//			beList.put(id, wBE);
	//			return id;
	//		}
	//		else if(element.getClass().equals(BusinessProcess.class)){
	//			WElement wBP=new WElement((BusinessProcess) element);
	//			wBP.setId(id);
	//			List<IArchimateElement> listElements = getTargetElementsByRelation(BusinessProcess.class, TriggeringRelationship.class, element);
	//			for(IArchimateElement nextElement:listElements){
	//				wBP.getIdTarget().put(addElementsRecursively(nextElement, beList, bpList,id), 1);
	//				if(source!=null){
	//					wBP.getIdSource().put(source, 1);
	//				}
	//			}
	//			List<IArchimateElement> listElements2 = getTargetElementsByRelation(BusinessEvent.class, TriggeringRelationship.class, element);
	//			for(IArchimateElement nextElement:listElements2){
	//				wBP.getIdTarget().put(addElementsRecursively(nextElement, beList, bpList,id), 1);
	//				if(source!=null){
	//					wBP.getIdSource().put(source, 1);
	//				}
	//			}
	//			bpList.put(id, wBP);
	//			return id;
	//		}
	//		else{
	//			return id;
	//		}
	//	}

	@Override
	public Object executeFunction() throws Exception {

		this.aBPGapAnalysisResult = new ArrayList<BusinessProcessGapAnalysisResult>();
		Set <Entry<String,WElement>> elementASIS=elementListASIS.entrySet();
		Set <Entry<String,WElement>> elementTOBE=elementListTOBE.entrySet();
		Set <Entry<String,WRelation>> relationshipASIS=relationshipListASIS.entrySet();
		Set<Entry<String, WRelation>> relationshipTOBE=relationshipListTOBE.entrySet();
		for(Entry<String,WElement> entry:elementASIS){
			String key=entry.getKey();
			WElement element=entry.getValue();
			WElement element2=elementListTOBE.get(key);
			if(element2==null){
				BusinessProcessGapAnalysisResult result=new BusinessProcessGapAnalysisResult(element.getId(), element.getName(), "DELETE","Element");
				aBPGapAnalysisResult.add(result);
			}
			else if(!element.equalProperties(element2)){
				BusinessProcessGapAnalysisResult result=new BusinessProcessGapAnalysisResult(element.getId(), element.getName(), "MODIFY PROPERTIES","Element");
				aBPGapAnalysisResult.add(result);
			}
			else if(!element.equalTarget(element2)){
				BusinessProcessGapAnalysisResult result=new BusinessProcessGapAnalysisResult(element.getId(), element.getName(), "MODIFY TARGETS","Element");
				aBPGapAnalysisResult.add(result);
			}
			else if(!element.equalSource(element2)){
				BusinessProcessGapAnalysisResult result=new BusinessProcessGapAnalysisResult(element.getId(), element.getName(), "MODIFY SOURCE","Element");
				aBPGapAnalysisResult.add(result);
			}
			else{
				BusinessProcessGapAnalysisResult result=new BusinessProcessGapAnalysisResult(element.getId(), element.getName(), "SAME","Element");
				aBPGapAnalysisResult.add(result);
			}
		}
		for(Entry<String,WElement> entry:elementTOBE){
			String key=entry.getKey();
			WElement element=entry.getValue();
			WElement element2=elementListASIS.get(key);
			if(element2==null){
				BusinessProcessGapAnalysisResult result=new BusinessProcessGapAnalysisResult(element.getId(), element.getName(), "NEW","Element");
				aBPGapAnalysisResult.add(result);
			}
		}
		return null;
	}

	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"ID","Type","Element","Status"};
		int[] widths = new int[]{100,200,200,200};
		createTable(headers, widths, this.aBPGapAnalysisResult, true, true );
		return null;
	}

	@Override
	public Boolean clearResults() {
		return true;
	}




	/**
	 * 
	 * @param diagramModelObj
	 * @param elements
	 * @throws Exception
	 */
	private void searchElementsRecursively(IDiagramModelObject diagramModelObj, Set<EObject> elements) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursively(diagramModelObjChild, elements);
			}

			elements.add(DiagramModelUtil.getModelElement(diagramModelObj));
		}
	}





}
