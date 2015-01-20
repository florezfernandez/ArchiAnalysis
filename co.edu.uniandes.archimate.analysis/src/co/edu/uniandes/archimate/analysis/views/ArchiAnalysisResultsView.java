package co.edu.uniandes.archimate.analysis.views;

import java.util.List;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import co.edu.uniandes.archimate.analysis.ArchiAnalysisPlugin;
import co.edu.uniandes.archimate.analysis.IArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.IResult;
import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.util.GEFUtil;

import com.archimatetool.editor.diagram.ArchimateDiagramEditor;
import com.archimatetool.model.IArchimateElement;

public class ArchiAnalysisResultsView extends ViewPart implements IArchiAnalysisView{

	public static String ID = ArchiAnalysisPlugin.PLUGIN_ID + ".views.ArchiAnalysisResultsView"; //$NON-NLS-1$

	private StructuredViewer viewer;
	private Composite parent;
	private Action clearAction;
	private Action doubleClickAction;
	private IArchiAnalysisFunction archiAnalysisFunction;
	private ArchimateDiagramEditor aDiagramEditor;

	public Composite getParent() {
		return parent;
	}

	/**
	 * The constructor.
	 */
	public ArchiAnalysisResultsView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		if(viewer!=null)
			viewer.getControl().setFocus();
	}

	@Override
	public StructuredViewer getViewer() {
		return this.viewer;
	}

	public void setViewer(StructuredViewer viewer) {
		this.viewer = viewer;
		this.viewer.refresh();
	}


	public TableViewer createTableView(IArchiAnalysisFunction archiAnalysisFunction,ArchimateDiagramEditor aDiagramEditor , List<? extends ITableResult> input,Table table)
	{
		this.archiAnalysisFunction = archiAnalysisFunction;
		TableViewer tViewer = new TableViewer(table);
		tViewer.setContentProvider(new ViewContentProvider());
		tViewer.setLabelProvider(new ViewLabelProvider());
		tViewer.setInput(input);
		this.setViewer(tViewer);
		this.getParent().layout(true);
		this.aDiagramEditor = aDiagramEditor;

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), ArchiAnalysisResultsView.ID);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();	
		contributeToActionBars();

		return tViewer;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ArchiAnalysisResultsView.this.fillContextMenu(manager);
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
				cleanView();
				archiAnalysisFunction.clearResults();
			}
		};
		clearAction.setText("Clear results");
		clearAction.setToolTipText("Clear all results");
		clearAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ArchiAnalysisPlugin.PLUGIN_ID, "icons/clear-16.png"));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if (obj instanceof IResult) {
					IResult result = (IResult)obj;
					IArchimateElement element = result.getElement();
					if(element!=null)
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

	public void cleanView()
	{
		ViewsUtil.hideViewPart(ID);
		ViewsUtil.showViewPart(ID, true);
	}


	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent instanceof List<?> ) {
				@SuppressWarnings("unchecked")
				List<ITableResult>  resultTableList = (List<ITableResult>)parent;
				return resultTableList.toArray();
			} else {
				return null;
			}
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if(obj instanceof ITableResult)
			{
				ITableResult iTableResult =(ITableResult)obj;
				return iTableResult.toArray()[index].toString();
			}
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			if(obj instanceof ITableResult)
			{
				ITableResult iTableResult = (ITableResult)obj;
				return iTableResult.getImage(index);
			}
			return null;
		}
	}

}