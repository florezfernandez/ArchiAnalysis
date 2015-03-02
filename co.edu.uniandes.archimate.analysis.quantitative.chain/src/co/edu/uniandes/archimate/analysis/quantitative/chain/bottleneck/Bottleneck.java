package co.edu.uniandes.archimate.analysis.quantitative.chain.bottleneck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.AssignmentRelationship;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessFunction;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class Bottleneck extends AbstractArchiAnalysisFunction{
	private final static int CAT11=1;
	private final static int CAT12=2;
	private final static int MAT=3;
	private final static int CAT2=4;
	private final static int CAT3=5;
	private int function;
	private List<BottleneckResultCAT> results1;
	private List<BottleneckResultMAT> results2;
	private List<BottleneckResultCAT2> results3;
	private HashMap<String,WElement> elements1;
	private Class<?> element1Class;
	private Class<?> element2Class;
	private HashMap<String,WElement> elements2;
	private List<WElement> orderedElements2;
	private Class<?> xRelationshipClass;
	private double percentageBottleneck;

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Bottleneck Chain";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		function=CAT3;
		percentageBottleneck=0.25;
		results1 = new ArrayList<BottleneckResultCAT>();
		results2 = new ArrayList<BottleneckResultMAT>();
		results3 = new ArrayList<BottleneckResultCAT2>();
		elements1 =new HashMap<String,WElement>();
		elements2 =new HashMap<String,WElement>();
		orderedElements2=new ArrayList<WElement>();
		element1Class=BusinessActor.class;
		element2Class=BusinessFunction.class;
		xRelationshipClass=AssignmentRelationship.class;
		ArrayList<IArchimateElement> list1=new ArrayList<IArchimateElement>();
		ArrayList<IArchimateElement> list2=new ArrayList<IArchimateElement>();

		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
			searchElementsRecursively(diagramModelObj, list1,element1Class);
			searchElementsRecursively(diagramModelObj, list2,element2Class);
		}
		for(IArchimateElement element:list1){
			WElement wElement=new WElement(element);
			elements1.put(wElement.getId(),wElement);
		}
		for(IArchimateElement element:list2){
			WElement wElement=new WElement(element);
			elements2.put(wElement.getId(),wElement);
			orderedAdd(wElement, orderedElements2);

		}
		@SuppressWarnings("unchecked")
		List<IRelationship> relationships = getRelations(IRelationship.class);
		for(IRelationship relationship:relationships){
			if(relationship.getClass().equals(xRelationshipClass)){
				String idSource=relationship.getSource().getId();
				String idTarget=relationship.getTarget().getId();
				elements1.get(idSource).getIdTarget().add(idTarget);
			}


		}



		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {

		if (function==CAT11){
			Collection<WElement> elements=(Collection<WElement>) elements1.values();
			for(WElement element:elements){
				BottleneckResultCAT result=new BottleneckResultCAT(element.getName(),element.getId());
				results1.add(result);
			}
		}
		else if(function==CAT12){
			Collection<WElement> elements=(Collection<WElement>) elements2.values();
			for(WElement element:elements){
				BottleneckResultCAT result=new BottleneckResultCAT(element.getName(),element.getId());
				results1.add(result);
			}
		}
		else if(function==MAT||function==CAT2||function==CAT3){
			Collection<WElement> elements=(Collection<WElement>) elements1.values();
			for(WElement element:elements){
				BottleneckResultMAT result=new BottleneckResultMAT(element, orderedElements2);
				results2.add(result);
			}
			if(function==CAT2||function==CAT3){
				for(BottleneckResultMAT res:results2){
					int count=0;
					Object[] array=res.toArray();
					for(int i=1;i<array.length;i++){
						if(array[i].equals("X")){
							count++;
						}
					}
					BottleneckResultCAT2 result=new BottleneckResultCAT2(array[0].toString(), count);
					results3.add(result);
				}
				if(function==CAT3){
					List<BottleneckResultCAT2> tempList=new ArrayList<BottleneckResultCAT2>();
					double total=(double)orderedElements2.size();
					for(BottleneckResultCAT2 res:results3){
						double count=(double)res.getCount();
						double perc=(double)count/total;
						if(perc>=percentageBottleneck){
							orderedAdd2(res, tempList);
						}
					}
					results3=tempList;
				}
			}
		}


		//		Map<Class<?>, Integer> elementsCount = new HashMap<Class<?>, Integer>();
		//		Set<EObject> elements = new HashSet<EObject>();
		//		
		//		for(IDiagramModelObject diagramModelObj: this.getDiagramModel().getChildren()){
		//			searchElementsRecursively(diagramModelObj, elements);
		//		}
		//		
		//		for(EObject eObject : elements){
		//			Class<?> eObjectClass = eObject.getClass();
		//			
		//			if(!elementsCount.containsKey(eObjectClass)){
		//				elementsCount.put(eObjectClass, (Integer)1);
		//			}else{
		//				Integer currentInstances = elementsCount.get(eObjectClass);
		//				currentInstances++;
		//				elementsCount.put(eObjectClass, (Integer)currentInstances);
		//			}
		//		}
		//		
		//		for(Entry<Class<?>, Integer> entry : elementsCount.entrySet()){
		////			System.out.println(entry.getKey().toString() + " - [" + entry.getValue().toString() + "]");
		//			BottleneckResult elementResult = new BottleneckResult(entry.getKey(), entry.getValue());
		//			results.add(elementResult);
		//		}

		return null;
	}

	

	/**
	 * 
	 * @param diagramModelObj
	 * @param elementList
	 * @throws Exception
	 */
	private void searchElementsRecursively(IDiagramModelObject diagramModelObj, List<IArchimateElement> elementList,Class<?> elementClass) throws Exception{
		if(diagramModelObj instanceof DiagramModelArchimateObject){
			DiagramModelArchimateObject archimateObject = ((DiagramModelArchimateObject)diagramModelObj);

			for(IDiagramModelObject diagramModelObjChild: archimateObject.getChildren()){
				searchElementsRecursively(diagramModelObjChild, elementList, elementClass);
			}

			if(DiagramModelUtil.getModelElement(diagramModelObj).getClass().equals(elementClass)){
				elementList.add((IArchimateElement) DiagramModelUtil.getModelElement(diagramModelObj));
			}			
		}
	}

	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{
		if(function==CAT11||function==CAT12){
			String[] headers = new String[]{"Name", "Element ID"};
			int[] widths = new int[]{200, 200};
			return createTable(headers, widths, results1, true, true);
		}
		else if(function==CAT2||function==CAT3){
			String[] headers = new String[]{"Element Name","Count"};
			int[] widths = new int[]{200,200};
			return createTable(headers, widths, results3, true, true);
		}
		else if(function==MAT){
			int length=orderedElements2.size();
			String[] headers=new String[length+1];
			int[] widths = new int[length+1];
			headers[0]="     ";
			widths[0]=100;
			for(int i=1;i<length+1;i++){
				headers[i]=orderedElements2.get(i-1).getName();
				widths[i]=100;
			}

			return createTable(headers, widths, results2, true, true);
		}
		// Table View
		else return null;

	}

	private void orderedAdd(WElement element, List<WElement> list){
		if(list.size()==0){
			list.add(element);
		}
		else{
			boolean fin=false;
			for(int i=0;i<list.size()&&!fin;i++){
				if(element.compare(list.get(i))>0){
					list.add(i, element);
					fin=true;
				}
			}
			if(!fin){
				list.add(element);
			}
		}
	}
	private void orderedAdd2(BottleneckResultCAT2 element,List<BottleneckResultCAT2> list) {
		
		if(list.size()==0){
			list.add(element);
		}
		else{
			boolean fin=false;
			for(int i=0;i<list.size()&&!fin;i++){
				if(element.getCount()>list.get(i).getCount()){
					list.add(i, element);
					fin=true;
				}
			}
			if(!fin){
				list.add(element);
			}
		}
		
	}




	/**
	 * 
	 */
	@Override
	public Boolean clearResults() {
		return true;
	}
}