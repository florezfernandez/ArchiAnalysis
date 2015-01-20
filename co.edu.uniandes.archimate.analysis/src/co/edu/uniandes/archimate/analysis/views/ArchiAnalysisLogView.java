package co.edu.uniandes.archimate.analysis.views;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ArchiAnalysisPlugin;
import co.edu.uniandes.archimate.analysis.entities.ValidationMessage;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.GEFUtil;

import com.archimatetool.editor.diagram.ArchimateDiagramEditor;
import com.archimatetool.model.IArchimateElement;

public class ArchiAnalysisLogView extends ViewPart implements IArchiAnalysisView{

	public static String ID = ArchiAnalysisPlugin.PLUGIN_ID + ".views.ArchiAnalysisLogView"; //$NON-NLS-1$

	private TableViewer viewer;
	private Action clearAction;
	private Action doubleClickAction;
	private ArchimateDiagramEditor aDiagramEditor;
	
	public ArchimateDiagramEditor getDiagramEditor() {
		return aDiagramEditor;
	}

	public void setDiagramEditor(ArchimateDiagramEditor aDiagramEditor) {
		this.aDiagramEditor = aDiagramEditor;
	}

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new Object[] {};
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if (index == 0) {
				if (obj instanceof ValidationMessage) {
					ValidationMessage validationMessage = (ValidationMessage) obj;
					return "  " + validationMessage.toString();
				}
			}
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			if (index == 0) {
				if (obj instanceof ValidationMessage) {
					ValidationMessage validationMessage = (ValidationMessage) obj;
					switch(validationMessage.getType()){
					case INFORMATION: 
						return AbstractUIPlugin.imageDescriptorFromPlugin(ArchiAnalysisPlugin.PLUGIN_ID, "icons/info-16.png").createImage();
					case WARNING: 
						return AbstractUIPlugin.imageDescriptorFromPlugin(ArchiAnalysisPlugin.PLUGIN_ID, "icons/warning-16.png").createImage();
					case ERROR: 
						return AbstractUIPlugin.imageDescriptorFromPlugin(ArchiAnalysisPlugin.PLUGIN_ID, "icons/error-16.png").createImage();
					}
				}
			}
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ArchiAnalysisLogView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), ArchiAnalysisLogView.ID);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ArchiAnalysisLogView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(clearAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(clearAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(clearAction);
	}

	private void makeActions() {
		clearAction = new Action() {
			public void run() {
				ArchiAnalysisLogView.cleanView();
			}
		};
		clearAction.setText("Clear log");
		clearAction.setToolTipText("Clear all log messages");
		clearAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ArchiAnalysisPlugin.PLUGIN_ID, "icons/clear-16.png"));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if (obj instanceof ValidationMessage) {
					ValidationMessage validationMessage = (ValidationMessage) obj;
					IArchimateElement element = validationMessage.getElement();
					GEFUtil.setFocus(aDiagramEditor, element);
				}
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public static void cleanView()
	{
		ArchiAnalysisLogView logView = (ArchiAnalysisLogView)ViewsUtil.findViewPart(ArchiAnalysisLogView.ID);
		TableViewer tViewer = (TableViewer)logView.getViewer();
		while(tViewer.getElementAt(0)!=null)
			tViewer.remove(tViewer.getElementAt(0));
	}
	
	public static void fillView(ArchimateDiagramEditor aDiagramEditor, ValidationResponse validationResponse, boolean cleanLog){
		if(cleanLog) cleanView();
		fillView(aDiagramEditor, validationResponse);
	}
	
	public static void fillView(ArchimateDiagramEditor aDiagramEditor, ValidationResponse validationResponse){
		ArchiAnalysisLogView logView = (ArchiAnalysisLogView)ViewsUtil.findViewPart(ArchiAnalysisLogView.ID);
		TableViewer tViewer = (TableViewer)logView.getViewer();
		tViewer.add(validationResponse.getMessages().toArray());
		logView.setDiagramEditor(aDiagramEditor);
		ViewsUtil.showViewPart(ArchiAnalysisLogView.ID, true);
	}
	
	@Override
	public StructuredViewer getViewer() {
		return this.viewer;
	}
}