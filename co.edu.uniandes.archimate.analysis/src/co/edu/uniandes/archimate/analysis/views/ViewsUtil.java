package co.edu.uniandes.archimate.analysis.views;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ViewsUtil {

	public static IViewPart findViewPart(String viewID) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		return page.findView(viewID);
	}

	public static  void toggleViewPart(String viewID, boolean activate) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewReference ref = page.findViewReference(viewID);
		if(ref != null) {
			page.hideView(ref);
		}
		else {
			showViewPart(viewID, activate);
		}
	}

	public static IViewPart showViewPart(String viewID, boolean activate) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart viewPart = null;
		try {
			viewPart = page.showView(viewID);
		}
		catch(PartInitException ex) {
			ex.printStackTrace();
		}

		return viewPart;
	}
	
    public static void hideViewPart(String viewID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewReference ref = page.findViewReference(viewID);
        if(ref != null) {
            page.hideView(ref);
        }
    }
}
