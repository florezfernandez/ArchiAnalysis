package co.edu.uniandes.archimate.analysis.util;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import co.edu.uniandes.archimate.analysis.util.ColorUtil.ColorType;

import com.archimatetool.editor.diagram.ArchimateDiagramEditor;
import com.archimatetool.editor.diagram.editparts.AbstractArchimateEditPart;
import com.archimatetool.editor.diagram.editparts.connections.AbstractDiagramConnectionEditPart;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.impl.DiagramModelNote;
import com.archimatetool.model.impl.DiagramModelObject;

public class GEFUtil {

	public static DiagramModelNote addNote(ArchimateDiagramEditor aDiagramEditor, String content, IArchimateElement element, boolean stickToSourceElement, int xOffset, int yOffset, int width, int height, String fillColor){
		int x = 0;
		int y = 0;
		try {
			EditPart editPart = GEFUtil.getEditPart(aDiagramEditor, element);
			if(editPart instanceof AbstractDiagramConnectionEditPart)
			{
				AbstractDiagramConnectionEditPart connectionEditPart = (AbstractDiagramConnectionEditPart)editPart;
				Connection connection = (Connection)connectionEditPart.getFigure();
				if(stickToSourceElement){
					x = connection.getPoints().getFirstPoint().x;
					y = connection.getPoints().getFirstPoint().y;
					if(connection.getPoints().getLastPoint().y <= connection.getPoints().getFirstPoint().y)
						y = y-height-2;
					else
						y = y+2;					
				}else{
					x = connection.getPoints().getLastPoint().x;
					y = connection.getPoints().getLastPoint().y;
					if(connection.getPoints().getLastPoint().y <= connection.getPoints().getFirstPoint().y)
						y = y+2;
					else
						y = y-height-2;
				}
				if(connection.getPoints().getLastPoint().x <= connection.getPoints().getFirstPoint().x)
					x = x-width-2;

			}if(editPart instanceof AbstractArchimateEditPart){
				AbstractArchimateEditPart elementEditPart = (AbstractArchimateEditPart)editPart;
				IFigure elementFigure = (IFigure)elementEditPart.getFigure();
				x = elementFigure.getClientArea().x;
				y = elementFigure.getClientArea().y;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		IBounds bounds = IArchimateFactory.eINSTANCE.createBounds(x+xOffset, y+yOffset, width, height);	

		return addNote(aDiagramEditor, content, bounds, fillColor);
	}

	public static DiagramModelNote addNote(ArchimateDiagramEditor aDiagramEditor, String content, IBounds bounds, String fillColor){
		DiagramModelNote note = (DiagramModelNote)IArchimateFactory.eINSTANCE.createDiagramModelNote();
		note.setContent(content);
		note.setFillColor(fillColor);
		note.setBounds(bounds);
		aDiagramEditor.getModel().getChildren().add(note);
		return note;
	}

	public static void removeNote(ArchimateDiagramEditor aDiagramEditor,DiagramModelNote note){
		aDiagramEditor.getModel().getChildren().remove(note);
	}

	public static void removeNotes(ArchimateDiagramEditor aDiagramEditor,List<DiagramModelNote> notes){
		for (DiagramModelNote diagramModelNote : notes) {
			removeNote(aDiagramEditor,diagramModelNote);
		}
	}


	public static ColorSheme[] changeColor(ArchimateDiagramEditor aDiagramEditor, IArchimateElement element, ColorSheme... colors)
	{
		List<ColorSheme> originalColors = new ArrayList<ColorSheme>();
		try {
			EditPart editPart = GEFUtil.getEditPart(aDiagramEditor, element);
			Object diagramModel = editPart.getModel();			
			for (ColorSheme colorShema : colors) {
				if(diagramModel instanceof IDiagramModelArchimateConnection)
				{
					IDiagramModelArchimateConnection diagramModelConnection = (IDiagramModelArchimateConnection)diagramModel;
					if(colorShema.getType().equals(ColorType.FontColor))
					{
						originalColors.add(new ColorSheme(ColorType.FontColor, diagramModelConnection.getFontColor()));
						diagramModelConnection.setFontColor(colorShema.getColor());
					}
					if(colorShema.getType().equals(ColorType.LineColor))
					{
						originalColors.add(new ColorSheme(ColorType.LineColor, diagramModelConnection.getLineColor()));
						diagramModelConnection.setLineColor(colorShema.getColor());
					}
				}else if(diagramModel instanceof DiagramModelObject){
					DiagramModelObject diagramModelObject = (DiagramModelObject)diagramModel;
					if(colorShema.getType().equals(ColorType.FontColor))
					{
						originalColors.add(new ColorSheme(ColorType.FontColor, diagramModelObject.getFontColor()));
						diagramModelObject.setFontColor(colorShema.getColor());
					}
					if(colorShema.getType().equals(ColorType.FillColor))
					{
						originalColors.add(new ColorSheme(ColorType.FillColor, diagramModelObject.getFillColor()));
						diagramModelObject.setFillColor(colorShema.getColor());
					}	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originalColors.toArray(new ColorSheme[originalColors.size()]) ;	
	}

	public static ColorSheme[] changeColor (ArchimateDiagramEditor aDiagramEditor, IArchimateElement element, String color){
		List<ColorSheme> originalColors = new ArrayList<ColorSheme>();
		try {
			EditPart editPart = GEFUtil.getEditPart(aDiagramEditor, element);
			Object diagramModel = editPart.getModel();
			if(diagramModel instanceof IDiagramModelArchimateConnection)
			{
				IDiagramModelArchimateConnection diagramModelConnection = (IDiagramModelArchimateConnection)diagramModel;
				originalColors.add(new ColorSheme(ColorType.FontColor, diagramModelConnection.getFontColor()));
				originalColors.add(new ColorSheme(ColorType.LineColor, diagramModelConnection.getLineColor()));
				diagramModelConnection.setFontColor(color);
				diagramModelConnection.setLineColor(color);
			}else if(diagramModel instanceof DiagramModelObject){
				DiagramModelObject diagramModelObject = (DiagramModelObject)diagramModel;
				originalColors.add(new ColorSheme(ColorType.FillColor, diagramModelObject.getFillColor()));
				diagramModelObject.setFillColor(color);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return originalColors.toArray(new ColorSheme[originalColors.size()]) ;	
	}

	public static void resize(EditPart editPart, int w,  int h) {
		ChangeBoundsRequest request = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
		request.setEditParts(editPart);
		request.setSizeDelta(new Dimension(w,h));

		Command cmd = editPart.getCommand(request);
		if (cmd != null && cmd.canExecute()) {
			cmd.execute();
		}
	}

	public static EditPart getEditPart(IDiagramModel diagramModel, GraphicalViewer gfViwer, IArchimateElement element) {
		EditPart editPart = null;
		List<IDiagramModelComponent> diagramModelComponents =  DiagramModelUtils.findDiagramModelComponentsForElement(diagramModel, element);
		if(!diagramModelComponents.isEmpty()) 
			editPart = (EditPart)gfViwer.getEditPartRegistry().get(diagramModelComponents.get(0));
		return editPart;
	}

	public static EditPart getEditPart(ArchimateDiagramEditor aDiagramEditor, IArchimateElement element){
		IDiagramModel diagramModel = aDiagramEditor.getModel();
		GraphicalViewer gfViwer = aDiagramEditor.getGraphicalViewer();		
		return getEditPart(diagramModel,gfViwer,element);
	}

	public static void setFocus(ArchimateDiagramEditor aDiagramEditor, IArchimateElement element){
		resetFocus(aDiagramEditor);
		if(element!=null){
			EditPart ep = GEFUtil.getEditPart(aDiagramEditor, element);
			GraphicalViewer gfViwer = aDiagramEditor.getGraphicalViewer();	
			gfViwer.select(ep);
		}
	}

	public static void resetFocus(ArchimateDiagramEditor aDiagramEditor){
		GraphicalViewer gfViwer = aDiagramEditor.getGraphicalViewer();	
		gfViwer.getSelectionManager().deselectAll();

	}


}
