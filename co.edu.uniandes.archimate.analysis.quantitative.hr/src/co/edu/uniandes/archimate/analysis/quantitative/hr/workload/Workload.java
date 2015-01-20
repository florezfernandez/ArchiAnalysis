package co.edu.uniandes.archimate.analysis.quantitative.hr.workload;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.entities.AnalysisProperty;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.entities.WeakTypedElement;
import co.edu.uniandes.archimate.analysis.util.ColorUtil;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;
import co.edu.uniandes.archimate.analysis.util.GEFUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.AssignmentRelationship;
import com.archimatetool.model.impl.BusinessProcess;
import com.archimatetool.model.impl.BusinessRole;
import com.archimatetool.model.impl.CompositionRelationship;
import com.archimatetool.model.impl.DiagramModelNote;

public class Workload extends AbstractArchiAnalysisFunction{

	public static final int DAILY_WORKING_HOURS = 8; //- d (hours): Daily working hours = 8 hours
	public static final int MINUTES_HOUR = 60; //- d (hours): Daily working hours = 8 hours
	private List<WBusinessProcess> wBusinessProcesses;
	private List<WorkloadResult> aWorkload;
	private List<DiagramModelNote> notes;

	@Override
	public String getName(){return "Human Resource workload at business process level analysis";}
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		wBusinessProcesses = new ArrayList<WBusinessProcess>();
		//get all process elements that contain the required property f: Daily arriving Instances.
		List<IArchimateElement> listProcesses = getWeakTypedElements(new WeakTypedElement(BusinessProcess.class, new AnalysisProperty("f",DiagramModelUtil.DEFFAUL_VALUE)));
		if(listProcesses.size()>0)
		{
			for (IArchimateElement process : listProcesses) {
				WBusinessProcess wBusinessProcess = new WBusinessProcess((BusinessProcess)process);
				wBusinessProcess.setF(DiagramModelUtil.getDoubleValue(process, "f"));
				if(wBusinessProcess.getF() == null) validationResponse.addError("Business Process Daily arriving Instances (f) has a wrong value.",process);
				//get all business subprocesses
				List<IArchimateElement> listSubprocesses = getTargetElementsByRelation(BusinessProcess.class, CompositionRelationship.class, process);
				if(listSubprocesses.size()==0)
					validationResponse.addError("Business process must contain at least one business subprocess.", process);
				for (IArchimateElement subprocess : listSubprocesses) {
					WBusinessSubprocess wBusinessSubprocess = new WBusinessSubprocess((BusinessProcess)subprocess);
					wBusinessSubprocess.setL(DiagramModelUtil.getDoubleValue(subprocess, "l"));
					if(wBusinessSubprocess.getL() == null) validationResponse.addError("Business Subprocess % of arriving instances (l) is not set or has a wrong value.", subprocess);
					wBusinessSubprocess.setT(DiagramModelUtil.getDoubleValue(subprocess, "t"));
					if(wBusinessSubprocess.getT() == null) validationResponse.addError("Business Subprocess average daily time spent per each instance expressed in minutes (t) is not set or has a wrong value.", subprocess);
					//get all business assignment relationships
					List<IRelationship> listAssignmentRelationship = getRelationsByRelationTypeAndTargetElement(AssignmentRelationship.class, subprocess);
					if(listAssignmentRelationship.size()==0)
						validationResponse.addError("Business Subprocess must source at least one assignment relationship.",subprocess);
					for (IRelationship assignmentRelationship : listAssignmentRelationship) {
						WAssignmentRelationship wAssignmentRelationship = new WAssignmentRelationship((AssignmentRelationship)assignmentRelationship);
						wAssignmentRelationship.setI(DiagramModelUtil.getDoubleValue(assignmentRelationship, "i"));
						if(wAssignmentRelationship.getI() == null) validationResponse.addError("Assignment Relationship % of involvement of the role in the subprocess (i) is not set or has a wrong value.",assignmentRelationship);
						wAssignmentRelationship.setC(DiagramModelUtil.getDoubleValue(assignmentRelationship, "c"));
						if(wAssignmentRelationship.getC() == null) validationResponse.addError("Assignment Relationship % of contribution of the role in the subprocess (c) is not set or has a wrong value.",assignmentRelationship);
						//get  business role targeted from relationships
						IArchimateElement businessRole = getSourceElementFromRelationsByElement(BusinessRole.class,assignmentRelationship);
						if(businessRole == null)
							validationResponse.addError("Assignment Relationship sourced by a business subprocess must have as target a business role.",assignmentRelationship);
						else 
						{
							WBusinessRole wBusinessRole = new WBusinessRole((BusinessRole)businessRole);
							wBusinessRole.setN(DiagramModelUtil.getDoubleValue(businessRole, "n"));
							if(wBusinessRole.getN() == null) validationResponse.addError("Business role number of employees (human resources) assigned to this role is not set or has a wrong value.",businessRole);
							wAssignmentRelationship.setWBusinessRole(wBusinessRole);
						}
						wBusinessSubprocess.addWAssignmentRelationship(wAssignmentRelationship);
					}
					wBusinessProcess.addWBusinessSubprocesses(wBusinessSubprocess);
				}
				wBusinessProcesses.add(wBusinessProcess);
			}

		}else{
			validationResponse.addError("No any process in the model has the required property f: Daily arriving Instances.");
		}

		return null;
	}	

	@Override
	public Object executeFunction() throws Exception {
		this.aWorkload = new ArrayList<WorkloadResult>();
		for (WBusinessProcess wBusinessProces : wBusinessProcesses) {
			for (WBusinessSubprocess wBusinessSubprocess : wBusinessProces.getWBusinessSubprocesses()) {
				for (WAssignmentRelationship wAssignmentRelationship : wBusinessSubprocess.getWAssignmentRelationships()) {
					WBusinessRole wBusinessRole = wAssignmentRelationship.getWBusinessRole();
					WorkloadResult a = new  WorkloadResult();

					a.process = wBusinessProces.getElement().getName().substring(0, wBusinessProces.getElement().getName().indexOf("(")).trim();
					a.subprocess = wBusinessSubprocess.getElement().getName().substring(0, wBusinessSubprocess.getElement().getName().indexOf("(")).trim();
					a.role = wBusinessRole.getElement().getName().substring(0, wBusinessRole.getElement().getName().indexOf("(")).trim();
					a.assignment = wAssignmentRelationship.getElement();

					a.f = wBusinessProces.getF();
					a.l = wBusinessSubprocess.getL();
					a.t = wBusinessSubprocess.getT();
					a.n = wBusinessRole.getN();
					a.i = wAssignmentRelationship.getI();
					a.c = wAssignmentRelationship.getC();
					a.ns = a.l/100 * a.f; 
					a.bt = a.t * a.ns / MINUTES_HOUR;
					a.it = a.i/100 * DAILY_WORKING_HOURS;
					a.ir = a.n * a.it;
					a.cr = a.c/100 * a.bt;
					a.y = a.cr / a.ir * 100;

					if(a.y>100) 			{a.desc=WorkloadResult.Overwork; }
					if(a.y<=100 && a.y>80) 	{a.desc=WorkloadResult.Appropriate;}
					if(a.y<=80 && a.y>50) 	{a.desc=WorkloadResult.Review;}
					if(a.y<=50) 			{a.desc=WorkloadResult.Idle;}

					aWorkload.add(a);
				}
			}
		}

		return null;
	}

	@Override
	public Object showResults () throws Exception{
		// Table View
		String[] headers = new String[]{"Process","Subprocess","Role","f","t","l","ns","bt","n","i","c","it","ir","cr","y","y Description"};
		int[] widths = new int[]{120,120,120,40,40,40,40,40,40,40,40,40,40,40,60,100};
		createTable(headers, widths, this.aWorkload, true, true );
		// Model
		if(notes != null)
			GEFUtil.removeNotes(getADiagramEditor(), notes);
		notes = new ArrayList<DiagramModelNote>();
		for (WorkloadResult result : aWorkload) {
			String fillColor = result.desc==WorkloadResult.Overwork? ColorUtil.Orange_Red:result.desc==WorkloadResult.Appropriate? ColorUtil.Medium_Spring_Green:result.desc==WorkloadResult.Review? ColorUtil.Bright_Gold:result.desc==WorkloadResult.Idle? ColorUtil.Orange_Red:ColorUtil.White;
			String content = WorkloadResult.DECIMAL_FORMAT.format(result.y) +"%"+ System.getProperty("line.separator") + result.desc;

			DiagramModelNote note = GEFUtil.addNote(getADiagramEditor(),content,result.assignment,true,0,0,80,35,fillColor);
			notes.add(note);
		}		
		return null;
	}

	@Override
	public Boolean clearResults() {
		
		if(notes != null)
			GEFUtil.removeNotes(getADiagramEditor(), notes);
		
		return true;
	}




}
