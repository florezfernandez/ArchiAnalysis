package co.edu.uniandes.archimate.analysis.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ShowAnalysisLogViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ViewsUtil.toggleViewPart(ArchiAnalysisLogView.ID, false);
        return null;
    }
    
}
