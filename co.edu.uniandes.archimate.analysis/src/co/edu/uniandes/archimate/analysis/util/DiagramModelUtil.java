package co.edu.uniandes.archimate.analysis.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import co.edu.uniandes.archimate.analysis.entities.AnalysisProperty;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedElement;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedRelation;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.AggregationRelationship;
import com.archimatetool.model.impl.CompositionRelationship;
import com.archimatetool.model.impl.DiagramModelGroup;
import com.archimatetool.model.impl.TriggeringRelationship;

public class DiagramModelUtil {

	public static final String DEFFAUL_VALUE = "**DEFAULT**"; // Default value wild card

	@SafeVarargs
	public static List<IArchimateElement> getCurrentViewElementsByType(IDiagramModel diagramModel,	Class<? extends IArchimateElement>... elements) {
		List<IArchimateElement> listElements = new ArrayList<IArchimateElement>();
		
		for(IDiagramModelObject diagramModelObj: diagramModel.getChildren()){
			try {
				EObject eObject = DiagramModelUtil.getModelElement(diagramModelObj);

				for(Class<? extends IArchimateElement> eClazz: elements){
					if( eClazz.isInstance(eObject)){
						listElements.add((IArchimateElement)eObject);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return listElements;
	}	

	public static List<IArchimateElement> getCurrentViewElementsByType(IDiagramModel diagramModel,	WeakTypedElement... weakTypedElements) {
		List<IArchimateElement> listElements = new ArrayList<IArchimateElement>();
		for(IDiagramModelObject diagramModelObj: diagramModel.getChildren())
			getCurrentViewElementsByTypeRecursive(listElements, diagramModelObj, weakTypedElements);
		return listElements;
	}

	private static void getCurrentViewElementsByTypeRecursive(List<IArchimateElement> listElements, IDiagramModelObject diagramModelObj, WeakTypedElement... weakTypedElements) {
		if(diagramModelObj instanceof DiagramModelGroup)
		{
			DiagramModelGroup group = (DiagramModelGroup)diagramModelObj;
			for(IDiagramModelObject diagramModelObjChild: group.getChildren())
				getCurrentViewElementsByTypeRecursive(listElements, diagramModelObjChild, weakTypedElements);
		}else{
			getCurrentViewElementsByTypeFinal(listElements, diagramModelObj, weakTypedElements);
		}
	}

	private static void getCurrentViewElementsByTypeFinal(List<IArchimateElement> listElements, IDiagramModelObject diagramModelObj, WeakTypedElement... weakTypedElements) {
		boolean valid;
		int countProperties;
		IArchimateElement eElement;
		try {
			EObject eObject = DiagramModelUtil.getModelElement(diagramModelObj);
			for(WeakTypedElement weakTypedElement: weakTypedElements)
			{
				if( weakTypedElement.getClazz().isInstance(eObject)){
					eElement = (IArchimateElement)eObject;
					valid = true;
					countProperties = 0;
					for (AnalysisProperty propertyT : weakTypedElement.getProperties()) {
						for(IProperty propertyS: eElement.getProperties()){
							if(propertyT.getKey().equals(propertyS.getKey())){
								countProperties++;
								if(!propertyT.getValue().equals(DEFFAUL_VALUE) && !propertyT.getValue().equals(propertyS.getValue())){
									valid = false;
								}
							}
						}
					}
					if(countProperties != weakTypedElement.getProperties().length) valid = false;
					if(valid) listElements.add(eElement);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SafeVarargs
	public static List<IRelationship> getCurrentViewRelationsByType(IDiagramModel diagramModel,	Class<? extends IRelationship>... relations) {
		List<IRelationship> listRelations = new ArrayList<IRelationship>();
		for(IDiagramModelObject diagramModelObj: diagramModel.getChildren())
		{
			try {
				for(IDiagramModelConnection diagramModelConn: diagramModelObj.getSourceConnections()){
					EObject eObjectConn = DiagramModelUtil.getModelRelationship(diagramModelConn);
					for(Class<? extends IRelationship> eClazz: relations)
					{
						if( eClazz.isInstance(eObjectConn)){
							listRelations.add((IRelationship)eObjectConn);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listRelations;
	}

	public static List<IRelationship> getCurrentViewRelationsByType(IDiagramModel diagramModel,	WeakTypedRelation... weakTypeRelations) {
		List<IRelationship> listRelations = new ArrayList<IRelationship>();
		boolean valid = true;
		int countProperties = 0;
		IRelationship eRelationship = null;
		for(IDiagramModelObject diagramModelObj: diagramModel.getChildren())
		{
			try {
				for(IDiagramModelConnection diagramModelConn: diagramModelObj.getSourceConnections()){
					EObject eObjectConn = DiagramModelUtil.getModelRelationship(diagramModelConn);
					for(WeakTypedRelation weakTypeRelation: weakTypeRelations)
					{
						if(weakTypeRelation.getClazz().isInstance(eObjectConn)){
							eRelationship = (IRelationship)eObjectConn;
							valid = true;
							countProperties = 0;
							for (AnalysisProperty propertyT : weakTypeRelation.getProperties()) {
								for(IProperty propertyS: eRelationship.getProperties()){
									if(propertyT.getKey().equals(propertyS.getKey())){
										countProperties++;
										if(!propertyT.getValue().equals(DEFFAUL_VALUE) && !propertyT.getValue().equals(propertyS.getValue())){
											valid = false;
										}
									}
								}
							}
							if(countProperties != weakTypeRelation.getProperties().length) valid = false;
							if(valid) listRelations.add(eRelationship);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listRelations;
	}

	@SafeVarargs
	public static List<IRelationship> getModelRelationsByType(IDiagramModel diagramModel,	Class<? extends IRelationship>... relations)
	{
		List<IRelationship> listRelations = new ArrayList<IRelationship>();
		for (EObject eObjectConn : diagramModel.getArchimateModel().getFolder(FolderType.RELATIONS).getElements()) {
			for(Class<? extends IRelationship> eClazz: relations)
			{
				if(eClazz.isInstance(eObjectConn)){
					listRelations.add((IRelationship)eObjectConn);
				}
			}
		}
		return listRelations;
	}

	
	public static List<IArchimateElement>  getSourceElementsByRelation(IDiagramModel diagramModel, Class<? extends IArchimateElement> sourceElementClazz, Class<? extends IRelationship> relationClazz, IArchimateElement targetElement){
		List<IArchimateElement> listElements = new ArrayList<IArchimateElement>();

		for (IRelationship iRelationship : getCurrentViewRelationsByType(diagramModel, relationClazz)) {
			if(iRelationship.getTarget().equals(targetElement)){
				listElements.add(iRelationship.getSource());
			}
		}

		return listElements;		
	}

	public static List<IArchimateElement>  getTargetElementsByRelation(IDiagramModel diagramModel, Class<? extends IArchimateElement> targetElementClazz, Class<? extends IRelationship> relationClazz, IArchimateElement sourceElement){
		List<IArchimateElement> listElements = new ArrayList<IArchimateElement>();
		if((relationClazz.equals(CompositionRelationship.class) || relationClazz.equals(AggregationRelationship.class)))
		{
			List<IArchimateElement> listElementsCurrentViewTargetElements= getCurrentViewElementsByType(diagramModel,targetElementClazz);

			for(IDiagramModelObject diagramModelObj: diagramModel.getChildren())
			{
				try {
					EObject eObject = DiagramModelUtil.getModelElement(diagramModelObj);
					if(eObject.equals(sourceElement))
					{
						for(EObject eChildDiagramModelObj: diagramModelObj.eContents())
						{
							if(IDiagramModelObject.class.isInstance(eChildDiagramModelObj)){
								EObject eChildObject =  DiagramModelUtil.getModelElement((IDiagramModelObject)eChildDiagramModelObj);
								if(targetElementClazz.isInstance(eChildObject))
									listElementsCurrentViewTargetElements.add((IArchimateElement)eChildObject);
							}
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (IRelationship iRelationship : getModelRelationsByType(diagramModel, relationClazz)) {
				if(iRelationship.getSource().equals(sourceElement) && listElementsCurrentViewTargetElements.contains(iRelationship.getTarget())){
					listElements.add(iRelationship.getTarget());
				}
			}
		}else{
			for (IRelationship iRelationship : getCurrentViewRelationsByType(diagramModel, relationClazz)) {
				if(iRelationship.getSource().equals(sourceElement)){
					listElements.add(iRelationship.getTarget());
				}
			}
		}

		return listElements;		
	}

	public static List<IRelationship> getRelationsByRelationTypeAndSourceElement(IDiagramModel diagramModel, Class<? extends IRelationship> relationClazz, IArchimateElement sourceElement) {
		List<IRelationship> listRelations =  new ArrayList<IRelationship>();

		for (IRelationship iRelationship : getCurrentViewRelationsByType(diagramModel, relationClazz)) {
			if(iRelationship.getSource().equals(sourceElement)){
				listRelations.add(iRelationship);
			}
		}

		return listRelations;
	}

	public static List<IRelationship> getRelationsByRelationTypeAndTargetElement(IDiagramModel diagramModel, Class<? extends IRelationship> relationClazz, IArchimateElement targetElement) {
		List<IRelationship> listRelations =  new ArrayList<IRelationship>();

		for (IRelationship iRelationship : getCurrentViewRelationsByType(diagramModel, relationClazz)) {
			if(iRelationship.getTarget().equals(targetElement)){
				listRelations.add(iRelationship);
			}
		}

		return listRelations;
	}

	public static IArchimateElement getTargetElementFromRelationsByElement(IDiagramModel diagramModel, Class<? extends IArchimateElement> targetElementClazz, IRelationship relationship) {
		EObject eObject = relationship.getTarget();
		if(targetElementClazz.isInstance(eObject))
			return (IArchimateElement)eObject;
		return null;
	}

	public static IArchimateElement getSourceElementFromRelationsByElement(IDiagramModel diagramModel, Class<? extends IArchimateElement> sourceElementClazz, IRelationship relationship) {
		EObject eObject = relationship.getSource();
		if(sourceElementClazz.isInstance(eObject))
			return (IArchimateElement)eObject;
		return null;
	}

	public static EObject getModelElement(IDiagramModelObject diagramModelObj)
			throws NoSuchFieldException, IllegalAccessException {
		Class<?> clazz=  diagramModelObj.getClass();
		Field f = clazz.getDeclaredField("fArchimateElement");
		f.setAccessible(true);
		EObject eObject = (EObject)f.get(diagramModelObj);
		return eObject;
	}

	public static EObject getModelRelationship(IDiagramModelConnection diagramModelConn)
			throws NoSuchFieldException, IllegalAccessException {
		Class<?> clazzConn=  diagramModelConn.getClass();
		Field f = clazzConn.getDeclaredField("fRelationship");
		f.setAccessible(true);
		EObject eObjectConn = (EObject)f.get(diagramModelConn);
		return eObjectConn;
	}

	public static String getValue(IArchimateElement element, String propertyKey) {
		try{
			String propertyValue = null;
			for(IProperty property: element.getProperties()){
				if(property.getKey().equals(propertyKey))
				{
					propertyValue = property.getValue();
					break;
				}
			}
			return propertyValue;
		} catch (Exception e) {
			return null;
		}
	}

	public static Double getDoubleValue(IArchimateElement element, String propertyKey) {
		try {
			String propertyValue = getValue(element, propertyKey);
			if(propertyValue == null) return null;
			return  Double.parseDouble(propertyValue);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getValue(IDiagramModel diagramModel, String propertyKey) {
		try{
			String propertyValue = null;
			for(IProperty property: diagramModel.getProperties()){
				if(property.getKey().equals(propertyKey))
				{
					propertyValue = property.getValue();
					break;
				}
			}
			return propertyValue;
		} catch (Exception e) {
			return null;
		}
	}

	public static Double getDoubleValue(IDiagramModel diagramModel, String propertyKey) {
		try {
			String propertyValue = getValue(diagramModel, propertyKey);
			if(propertyValue == null) return null;
			return  Double.parseDouble(propertyValue);
		} catch (Exception e) {
			return null;
		}
	}
}
