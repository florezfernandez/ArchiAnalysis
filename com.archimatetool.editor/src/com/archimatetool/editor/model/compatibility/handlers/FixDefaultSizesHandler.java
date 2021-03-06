/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.editor.diagram.figures.diagram.GroupFigure;
import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ICompatibilityHandler;
import com.archimatetool.editor.ui.factory.ElementUIFactory;
import com.archimatetool.editor.ui.factory.IElementUIProvider;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IJunctionElement;



/**
 *  In Archi versions >= 3.0.0 We no longer save default widths and heights as -1, -1
 * 
 * @author Phillip Beauvoir
 */
public class FixDefaultSizesHandler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();

            if(element instanceof IDiagramModelObject) {
                IDiagramModelObject dmo = (IDiagramModelObject)element;
                IBounds bounds = dmo.getBounds();
                Dimension d = getNewSize(dmo);
                bounds.setWidth(d.width);
                bounds.setHeight(d.height);
            }
        }
    }
    
    Dimension getNewSize(IDiagramModelObject dmo) {
        IBounds bounds = dmo.getBounds();
        if(bounds.getWidth() != -1 && bounds.getHeight() != -1) {
            return new Dimension(bounds.getWidth(), bounds.getHeight());
        }
        
        // Calculate default size based on children
        if(dmo instanceof IDiagramModelContainer && ((IDiagramModelContainer)dmo).getChildren().size() > 0) {
            IDiagramModelContainer container = (IDiagramModelContainer)dmo;
            // Start with zero and build up from that...
            Dimension childrenSize = new Dimension();

            for(IDiagramModelObject child : container.getChildren()) {
                IBounds childbounds = child.getBounds();
                Dimension size = getNewSize(child);
                childrenSize.width = Math.max(childbounds.getX() + size.width() + 10, childrenSize.width);
                childrenSize.height = Math.max(childbounds.getY() + size.height() + 10, childrenSize.height);
            }
            
            Dimension defaultSize = getDefaultSize(dmo);
            Dimension newSize = childrenSize.union(defaultSize);
            
            // Compensate for Group content pane offset (this is a bad kludge)
            if(dmo instanceof IDiagramModelGroup && newSize.height > defaultSize.height) {
                newSize.height += GroupFigure.TOPBAR_HEIGHT;
            }

            return newSize;
        }
        
        // No children...
        return getDefaultSize(dmo);
    }

    Dimension getDefaultSize(IDiagramModelObject dmo) {
        IBounds bounds = dmo.getBounds();
        if(bounds.getWidth() != -1 && bounds.getHeight() != -1) {
            return new Dimension(bounds.getWidth(), bounds.getHeight());
        }

        // Legacy size of ArchiMate figure
        if(dmo instanceof IDiagramModelArchimateObject) {
            if(!(((IDiagramModelArchimateObject)dmo).getArchimateElement() instanceof IJunctionElement)) {
                return new Dimension(120, 55);
            }
        }
        
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(dmo);
        return provider != null ? provider.getDefaultSize() : new Dimension(120, 55);
    }
}
