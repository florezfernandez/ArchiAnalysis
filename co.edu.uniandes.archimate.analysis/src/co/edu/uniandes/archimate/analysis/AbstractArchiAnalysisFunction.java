package co.edu.uniandes.archimate.analysis;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedElement;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedRelation;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;
import co.edu.uniandes.archimate.analysis.views.ArchiAnalysisLogView;
import co.edu.uniandes.archimate.analysis.views.ArchiAnalysisResultsView;
import co.edu.uniandes.archimate.analysis.views.ViewsUtil;

import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.editor.diagram.ArchimateDiagramEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IRelationship;

public class AbstractArchiAnalysisFunction extends AbstractModelSelectionHandler  implements IArchiAnalysisFunction {

	private ArchimateDiagramEditor aDiagramEditor;
	private IDiagramModel diagramModel;
	private GraphicalViewer gfViwer;
	private ValidationResponse validationResponse;

	protected ArchimateDiagramEditor getADiagramEditor() {
		return aDiagramEditor;
	}

	protected IDiagramModel getDiagramModel() {
		return diagramModel;
	}

	protected GraphicalViewer getGfViwer() {
		return gfViwer;
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IArchimateModel model = getActiveArchimateModel();
		
		if(model != null) {
			this.aDiagramEditor = (ArchimateDiagramEditor)workbenchWindow.getActivePage().getActiveEditor();
			this.diagramModel = aDiagramEditor.getModel();
			this.gfViwer = EditorManager.openDiagramEditor(diagramModel).getGraphicalViewer();

			validationResponse = new ValidationResponse(this.diagramModel.getName() + " - "+ this.getName() + " Validation results");
			try {
				ViewsUtil.hideViewPart(ArchiAnalysisResultsView.ID);
				this.validateModel(validationResponse);
				if(validationResponse.isValid()){
					validationResponse.addInfo("No validation errors.");
					pritValidation(validationResponse);
					this.executeFunction();
					ViewsUtil.showViewPart(ArchiAnalysisResultsView.ID, true);
					this.showResults();
				}else{
					pritValidation(validationResponse);
				}
			} catch (Exception e) {
				validationResponse.addError(e.getMessage());
				ArchiAnalysisLogView.fillView(this.aDiagramEditor,validationResponse,true);				
				e.printStackTrace();
			}

			gfViwer.deselectAll();

		}

		return null;
	}

	private void pritValidation(ValidationResponse validationResponse){
		ArchiAnalysisLogView.fillView(this.aDiagramEditor,validationResponse,true);
		System.out.print(validationResponse.toString());
	}

	@SuppressWarnings("unchecked")
	protected List<IRelationship> getRelations(Class<? extends IRelationship>... relations)
	{
		List<IRelationship> listRelations =  DiagramModelUtil.getCurrentViewRelationsByType(this.diagramModel, relations);
		return listRelations;
	}

	@SuppressWarnings("unchecked")
	protected List<IArchimateElement> getElements(Class<? extends IArchimateElement>... elements)
	{
		List<IArchimateElement> listElements =  DiagramModelUtil.getCurrentViewElementsByType(this.diagramModel, elements);
		return listElements;
	}

	protected List<IRelationship> getWeakTypedRelations(WeakTypedRelation... weakTypeRelations)
	{
		List<IRelationship> listRelations =  DiagramModelUtil.getCurrentViewRelationsByType(this.diagramModel, weakTypeRelations);
		return listRelations;
	}

	protected List<IArchimateElement> getWeakTypedElements(WeakTypedElement... weakTypedElements)
	{
		List<IArchimateElement> listElements =  DiagramModelUtil.getCurrentViewElementsByType(this.diagramModel, weakTypedElements);
		return listElements;
	}

	protected List<IArchimateElement> getSourceElementsByRelation(Class<? extends IArchimateElement> sourceElementClazz, Class<? extends IRelationship> relationClazz, IArchimateElement targetElement)
	{
		List<IArchimateElement> listElements =  DiagramModelUtil.getSourceElementsByRelation(this.diagramModel, sourceElementClazz,relationClazz,targetElement);
		return listElements;		
	}

	protected List<IArchimateElement> getTargetElementsByRelation(Class<? extends IArchimateElement> targetElementClazz, Class<? extends IRelationship> relationClazz, IArchimateElement sourceElement)
	{
		List<IArchimateElement> listElements =  DiagramModelUtil.getTargetElementsByRelation(this.diagramModel, targetElementClazz,relationClazz,sourceElement);
		return listElements;		
	}

	protected List<IRelationship> getRelationsByRelationTypeAndSourceElement(Class<? extends IRelationship> relationClazz, IArchimateElement sourceElement) {
		List<IRelationship> listRelations =  DiagramModelUtil.getRelationsByRelationTypeAndSourceElement(this.diagramModel, relationClazz, sourceElement);
		return listRelations;
	}

	protected List<IRelationship> getRelationsByRelationTypeAndTargetElement(Class<? extends IRelationship> relationClazz, IArchimateElement targetElement) {
		List<IRelationship> listRelations =  DiagramModelUtil.getRelationsByRelationTypeAndTargetElement(this.diagramModel, relationClazz, targetElement);
		return listRelations;
	}

	protected IArchimateElement getTargetElementFromRelationsByElement(Class<? extends IArchimateElement> targetElementClazz, IRelationship relationship) {
		return  DiagramModelUtil.getTargetElementFromRelationsByElement(this.diagramModel, targetElementClazz, relationship);
	}

	protected IArchimateElement getSourceElementFromRelationsByElement(Class<? extends IArchimateElement> sourceElementClazz, IRelationship relationship) {
		return  DiagramModelUtil.getSourceElementFromRelationsByElement(this.diagramModel, sourceElementClazz, relationship);
	}

	protected TableViewer createTable(String[] headers, int[] widths, List<? extends ITableResult> input, boolean headerVisible, boolean linesVisible) {
		ArchiAnalysisResultsView view = (ArchiAnalysisResultsView)ViewsUtil.findViewPart(ArchiAnalysisResultsView.ID);
		Table table = new Table(view.getParent(), SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(headerVisible);
		table.setLinesVisible(linesVisible);
		TableColumn[] columns = new TableColumn[headers.length];

		for (int i = 0; i < columns.length; i++) {
			columns[i]=new TableColumn(table, SWT.LEFT);
			columns[i].setText(headers[i]);
			columns[i].setWidth(widths[i]);
		}
		
		return view.createTableView(this, aDiagramEditor, input, table);
	}	

	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		this.validationResponse.addError("Validation method was not implemented in the analysis function.");
		return null;
	}

	@Override
	public Object executeFunction() throws Exception{
		this.validationResponse.addError("Execute method was not implemented in the analysis function.");
		return null;
	}

	@Override
	public String getName(){
		return "No name";
	}

	@Override
	public Object showResults () throws Exception{
		return null;
	}

	@Override
	public Boolean clearResults() {

		return true;
	}
}
