/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.figures.diagram.ArchimateDiagramModelGraphicsIcon;
import com.archimatetool.editor.diagram.sketch.figures.SketchModelGraphicsIcon;
import com.archimatetool.editor.ui.factory.ElementUIFactory;
import com.archimatetool.editor.ui.factory.IElementUIProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.ISketchModel;



/**
 * Main Label Provider for Archimate Editor
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateLabelProvider implements IEditorLabelProvider {
    
    public static ArchimateLabelProvider INSTANCE = new ArchimateLabelProvider();

    /**
     * @return A default name for an element.<br>
     *         This will be the element's name if of type INameable or the default Archimate name if an Archimate EObject,
     *         or a default name if blank.
     */
    @Override
    public String getLabel(Object element) {
        if(element == null) {
            return ""; //$NON-NLS-1$
        }
        
        String name = null;
        
        // Get Name
        if(element instanceof INameable) {
            name = ((INameable)element).getName();
        }
        
        // It's blank. Can we get a default name from its class?
        if(!StringUtils.isSet(name) && element instanceof EObject) {
            name = getDefaultName(((EObject)element).eClass());
        }
        
        // Yes
        if(StringUtils.isSet(name)) {
            return name;
        }
        
        // Defaults for empty strings
        if(element instanceof IArchimateDiagramModel) {
            return Messages.ArchimateLabelProvider_0;
        }
        else if(element instanceof ISketchModel) {
            return Messages.ArchimateLabelProvider_1;
        }
        else if(element instanceof IDiagramModelImage) {
            return Messages.ArchimateLabelProvider_2;
        }
       
        // If it's blank try registered extensions
        if(!StringUtils.isSet(name)) {
            name = LabelProviderExtensionHandler.INSTANCE.getLabel(element);
        }
        
        return StringUtils.safeString(name);
    }

    @Override
    public Image getImage(Object element) {
        if(element == null) {
            return null;
        }
        
        Image image = null;
        
        // This first, since EClass is an EObject
        if(element instanceof EClass) {
            image = getEClassImage((EClass)element);
        }
        else if(element instanceof EObject) {
            image = getObjectImage(((EObject)element));
        }
        
        // Try registered extensions
        if(image == null) {
            image = LabelProviderExtensionHandler.INSTANCE.getImage(element);
        }
        
        return image;
    }
    
    @Override
    public IGraphicsIcon getGraphicsIcon(Object element) {
        if(element == null) {
            return null;
        }
        
        IGraphicsIcon graphicsIcon = null;
        
        if(element instanceof IArchimateDiagramModel) {
            graphicsIcon = new ArchimateDiagramModelGraphicsIcon();
        }
        else if(element instanceof ISketchModel) {
            graphicsIcon = new SketchModelGraphicsIcon();
        }
        
        // Try registered extensions
        if(graphicsIcon == null) {
            graphicsIcon = LabelProviderExtensionHandler.INSTANCE.getGraphics2dIcon(element);
        }

        return graphicsIcon;
    }
    
    /**
     * @return An Image for an EObject instance
     */
    private Image getObjectImage(EObject eObject) {
        if(eObject == null) {
            return null;
        }
        
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eObject.eClass());
        if(provider != null) {
            return provider.getImage(eObject);
        }
        
        return getEClassImage(eObject.eClass());
    }
    
    /**
     * @return An Image for an EClass
     */
    private Image getEClassImage(EClass eClass) {
        if(eClass == null) {
            return null;
        }
        
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getImage();
        }
        
        String imageName = getImageName(eClass);
        if(imageName != null) {
            return IArchimateImages.ImageFactory.getImage(imageName);
        }
        
        return null;
    }

    /**
     * @param eClass
     * @return An ImageDescriptor for an EClass
     */
    public ImageDescriptor getImageDescriptor(EClass eClass) {
        if(eClass == null) {
            return null;
        }
        
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getImageDescriptor();
        }
        
        String imageName = getImageName(eClass);
        if(imageName != null) {
            return IArchimateImages.ImageFactory.getImageDescriptor(imageName);
        }
        
        return null;
    }
    
    private String getImageName(EClass eClass) {
        if(eClass == null) {
            return null;
        }
        
        switch(eClass.getClassifierID()) {
            // Other
            case IArchimatePackage.ARCHIMATE_MODEL:
                return IArchimateImages.ICON_MODELS_16;
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL:
                return IArchimateImages.ICON_DIAGRAM_16;
            case IArchimatePackage.FOLDER:
                return IArchimateImages.ECLIPSE_IMAGE_FOLDER;
            
            // Sketch
            case IArchimatePackage.SKETCH_MODEL:
                return IArchimateImages.ICON_SKETCH_16;
        }
        
        return null;
    }
    
    /**
     * Get a default human-readable name for an EClass
     * @param eClass The Class
     * @return A name or null
     */
    public String getDefaultName(EClass eClass) {
        if(eClass == null) {
            return ""; //$NON-NLS-1$
        }
        
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getDefaultName();
        }
        
        return ""; //$NON-NLS-1$
    }
    
    /**
     * Get a default human-readable short name for an EClass
     * @param eClass The Class
     * @return A name or null
     */
    public String getDefaultShortName(EClass eClass) {
        if(eClass == null) {
            return ""; //$NON-NLS-1$
        }
        
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getDefaultShortName();
        }
        
        return ""; //$NON-NLS-1$
    }

    /**
     * @param relation
     * @return A sentence that describes the relationship between the relation's source and target elements
     */
    public String getRelationshipSentence(IRelationship relation) {
        if(relation != null) {
            if(relation.getSource() != null && relation.getTarget() != null) {
                String nameSource = ArchimateLabelProvider.INSTANCE.getLabel(relation.getSource());
                String nameTarget = ArchimateLabelProvider.INSTANCE.getLabel(relation.getTarget());
                
                switch(relation.eClass().getClassifierID()) {
                    case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_3, nameSource, nameTarget);

                    case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_4, nameSource, nameTarget);

                    case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_5, nameSource, nameTarget);

                    case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_6, nameSource, nameTarget);

                    case IArchimatePackage.FLOW_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_7, nameSource, nameTarget);

                    case IArchimatePackage.ACCESS_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_8, nameSource, nameTarget);

                    case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_9, nameSource, nameTarget);

                    case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_10, nameSource, nameTarget);

                    case IArchimatePackage.REALISATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_11, nameSource, nameTarget);

                    case IArchimatePackage.USED_BY_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_12, nameSource, nameTarget);

                    case IArchimatePackage.INFLUENCE_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_13, nameSource, nameTarget);

                    default:
                        return ""; //$NON-NLS-1$
                }
            }
        }
        
        return ""; //$NON-NLS-1$
    }

    /**
     * @param eClass The Relationship class
     * @param reverseDirection If this is true then the phrase is from target to source
     * @return A phrase that describes the relationship, for example "Is Realised by", "Flows to"
     */
    public String getRelationshipPhrase(EClass eClass, boolean reverseDirection) {
        if(eClass == null) {
            return ""; //$NON-NLS-1$
        }
        
        switch(eClass.getClassifierID()) {
            case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_14 : Messages.ArchimateLabelProvider_15;

            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_16 : Messages.ArchimateLabelProvider_17;

            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_18 : Messages.ArchimateLabelProvider_19;

            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_20 : Messages.ArchimateLabelProvider_21;

            case IArchimatePackage.FLOW_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_22 : Messages.ArchimateLabelProvider_23;

            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_24 : Messages.ArchimateLabelProvider_25;

            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_26 : Messages.ArchimateLabelProvider_27;

            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_28 : Messages.ArchimateLabelProvider_29;

            case IArchimatePackage.REALISATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_30 : Messages.ArchimateLabelProvider_31;

            case IArchimatePackage.USED_BY_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_32 : Messages.ArchimateLabelProvider_33;

            case IArchimatePackage.INFLUENCE_RELATIONSHIP:
                return reverseDirection ? Messages.ArchimateLabelProvider_34 : Messages.ArchimateLabelProvider_35;

            default:
                return ""; //$NON-NLS-1$
        }
    }
}
