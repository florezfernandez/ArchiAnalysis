/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.INameable;



/**
 * Image Manager Dialog
 * 
 * @author Phillip Beauvoir
 */
public class ImageManagerDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.ImageManagerDialog"; //$NON-NLS-1$
    
    protected static final String OPEN = Messages.ImageManagerDialog_0;
    
    protected int DEFAULT_GALLERY_ITEM_SIZE = 128;
    protected int MIN_GALLERY_ITEM_SIZE = 64;
    protected int MAX_GALLERY_ITEM_SIZE = 256;
    
    protected Gallery fGallery;
    protected GalleryItem fGalleryRoot;
    protected Scale fScale;
    protected ModelsViewer fModelsViewer;

    private Object fSelectedObject;
    
    private IArchimateModel fSelectedModel;
    private String fSelectedImagePath;
    
    private Map<String, Image> fImageCache = new HashMap<String, Image>();

    public ImageManagerDialog(Shell parentShell, IArchimateModel selectedModel, String selectedImagePath) {
        super(parentShell, "ImageManagerDialog"); //$NON-NLS-1$
        setTitleImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fSelectedModel = selectedModel;
        fSelectedImagePath = selectedImagePath;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.ImageManagerDialog_1);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.ImageManagerDialog_1);
        setMessage(Messages.ImageManagerDialog_2);
        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        SashForm sash = new SashForm(client, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 580;
        gd.heightHint = 300;
        sash.setLayoutData(gd);
        
        // Table
        Composite tableComp = new Composite(sash, SWT.BORDER);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        tableComp.setLayout(layout);
        
        CLabel label = new CLabel(tableComp, SWT.NULL);
        label.setText(Messages.ImageManagerDialog_3);
        
        Composite tableComp2 = new Composite(tableComp, SWT.NULL);
        tableComp2.setLayout(new TableColumnLayout());
        tableComp2.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fModelsViewer = new ModelsViewer(tableComp2);
        fModelsViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        fModelsViewer.setInput(""); //$NON-NLS-1$
        fModelsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IArchimateModel) {
                    fScale.setEnabled(true);
                    clearGallery();
                    updateGallery((IArchimateModel)o);
                }
            }
        });
        
        // Mouse Up action...
        fModelsViewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                Object o = ((IStructuredSelection)fModelsViewer.getSelection()).getFirstElement();
                // Open...
                if(o == OPEN) {
                    handleOpenAction();
                }
            }
        });

        Composite galleryComposite = new Composite(sash, SWT.NULL);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        galleryComposite.setLayout(layout);
        
        fGallery = new Gallery(galleryComposite, SWT.V_SCROLL | SWT.BORDER);
        fGallery.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Renderers
        final NoGroupRenderer groupRenderer = new NoGroupRenderer();
        groupRenderer.setItemSize(DEFAULT_GALLERY_ITEM_SIZE, DEFAULT_GALLERY_ITEM_SIZE);
        groupRenderer.setAutoMargin(true);
        groupRenderer.setMinMargin(10);
        fGallery.setGroupRenderer(groupRenderer);
        
        final DefaultGalleryItemRenderer itemRenderer = new DefaultGalleryItemRenderer();
        itemRenderer.setDropShadows(true);
        itemRenderer.setDropShadowsSize(7);
        itemRenderer.setShowRoundedSelectionCorners(false);
        fGallery.setItemRenderer(itemRenderer);
        
        // Root Group
        fGalleryRoot = new GalleryItem(fGallery, SWT.NONE);
        
        // Slider
        fScale = new Scale(galleryComposite, SWT.HORIZONTAL);
        gd = new GridData(SWT.END, SWT.NONE, false, false);
        gd.widthHint = 120;
        if(PlatformUtils.isMac()) { // Mac clips height of slider
            gd.heightHint = 18;
        }
        fScale.setLayoutData(gd);
        fScale.setMinimum(MIN_GALLERY_ITEM_SIZE);
        fScale.setMaximum(MAX_GALLERY_ITEM_SIZE);
        fScale.setIncrement(8);
        fScale.setPageIncrement(32);
        fScale.setSelection(DEFAULT_GALLERY_ITEM_SIZE);
        fScale.setEnabled(false);
        fScale.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int inc = fScale.getSelection();
                itemRenderer.setDropShadows(inc >= 96);
                groupRenderer.setItemSize(inc, inc);
            }
        });

        // Gallery selections
        fGallery.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(e.item instanceof GalleryItem) {
                    fSelectedObject = ((GalleryItem)e.item).getData();
                }
                else {
                    fSelectedObject = null;
                }
             }
        });
        
        // Double-clicks
        fGallery.addListener(SWT.MouseDoubleClick, new Listener() {
            public void handleEvent(Event event) {
                GalleryItem item = fGallery.getItem(new Point(event.x, event.y));
                if(item != null) {
                    okPressed();
                }
            }
        });
        
        // Dispose of the images here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this control is asked to relayout.
        fGallery.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposeImages();
            }
        });

        sash.setWeights(new int[] { 30, 70 });
        
        /*
         * Select the given model and image
         * Better to put this on a thread as selection sometimes doesn't happen
         */
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                // Make selection of model in table if it has images
                if(fSelectedModel != null) {
                    IArchiveManager archiveManager = (IArchiveManager)fSelectedModel.getAdapter(IArchiveManager.class);
                    if(archiveManager.hasImages()) {
                        // Select model
                        fModelsViewer.setSelection(new StructuredSelection(fSelectedModel));
                        
                        // Make selection of image if set
                        if(fSelectedImagePath != null) {
                            for(GalleryItem item : fGalleryRoot.getItems()) {
                                String imagePath = (String)item.getData();
                                if(imagePath != null && fSelectedImagePath.equals(imagePath)) {
                                    fGallery.setSelection(new GalleryItem[] { item });
                                    fSelectedObject = imagePath; // we need to do this here because this is on a thread
                                    break;
                                }
                            }
                        }
                    }
                    // Else select the first valid model that's open
                    else {
                        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                            archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
                            if(archiveManager.hasImages()) {
                                fModelsViewer.setSelection(new StructuredSelection(model));
                                break;
                            }
                        }
                    }
                }
            }
        });
        
        return composite;
    }
    
    /**
     * Clear old root group
     */
    protected void clearGallery() {
        if(fGalleryRoot != null && !fGallery.isDisposed() && fGallery.getItemCount() > 0) {
            while(fGalleryRoot.getItemCount() > 0) {
                GalleryItem item = fGalleryRoot.getItem(0);
                fGalleryRoot.remove(item);
            }
        }
    }

    protected void updateGallery(final IArchimateModel model) {
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
                
                for(String path : archiveManager.getImagePaths()) {
                    Image thumbnail = fImageCache.get(path);
                    if(thumbnail == null) {
                        try {
                            Image image = archiveManager.createImage(path);
                            if(image != null) {
                                thumbnail = ImageFactory.getScaledImage(image, MAX_GALLERY_ITEM_SIZE);
                                image.dispose();
                                fImageCache.put(path, thumbnail);
                            }
                        }
                        catch(Exception ex) {
                            ex.printStackTrace();
                            continue;
                        }
                    }
                    
                    GalleryItem item = new GalleryItem(fGalleryRoot, SWT.NONE);
                    item.setImage(thumbnail);
                    item.setData(path);
                }
                
                fGallery.redraw(); // at some scale settings this is needed
            }
        });
    }

    /**
     * User wants to open Image from file
     */
    protected void handleOpenAction() {
        getShell().setVisible(false);

        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.png;*.jpg;*.jpeg;*.gif;*.tif;*.tiff;*.bmp;*.ico", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        String path = dialog.open();
        if(path != null) {
            fSelectedObject = new File(path);
            okPressed();
        }
        else {
            getShell().setVisible(true);
        }
    }
    
    /**
     * @return The selected object which is either a File to an image or a String path to a loaded image
     */
    public Object getSelectedObject() {
        return fSelectedObject;
    }
    
    protected void disposeImages() {
        for(Entry<String, Image> entry : fImageCache.entrySet()) {
            Image image = entry.getValue();
            if(image != null && !image.isDisposed()) {
                image.dispose();
            }
        }
    }
    
    protected class ModelsViewer extends TableViewer {
        public ModelsViewer(Composite parent) {
            super(parent, SWT.FULL_SELECTION);
            setColumns();
            setContentProvider(new ModelsViewerContentProvider());
            setLabelProvider(new ModelsViewerLabelProvider());
            setSorter(new ViewerSorter() {
                @Override
                public int category(Object element) {
                    if(element == OPEN) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        
        protected void setColumns() {
            Table table = getTable();
            table.setHeaderVisible(false);
            
            // Use layout from parent container
            TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
            TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
            layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        }

        protected class ModelsViewerContentProvider implements IStructuredContentProvider {
            @Override
            public void dispose() {
            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                List<Object> list = new ArrayList<Object>();
                
                for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                    IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
                    if(archiveManager.hasImages()) {
                        list.add(model);
                    }
                }
                
                list.add(OPEN);

                return list.toArray();
            }
        }

        protected class ModelsViewerLabelProvider extends LabelProvider {
            @Override
            public String getText(Object element) {
                if(element instanceof INameable) {
                    return ((INameable)element).getName();
                }
                return super.getText(element);
            }
            
            @Override
            public Image getImage(Object element) {
                return ArchimateLabelProvider.INSTANCE.getImage(element);
            }
        }
    }
}
