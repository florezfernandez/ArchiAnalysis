package co.edu.uniandes.archimate.analysis.functional.hr.raci;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class DecompositionFunctionalxSWT {
	
	//-----------------------------
	//Attributes
	//-----------------------------
	private Display display;
	private Shell shell;
	private String orgUnitVsActorMatrix;
	private String actorVsRoleMatrix;
	private String raciMatrix;
	private String fileSave;
	
	
	//----------------------------
	//Constructors
	//----------------------------
	
	
	public DecompositionFunctionalxSWT(Display d){
		if(d!=null){
		display=d;
		shell=new Shell(display);
		}
	}
	
	public DecompositionFunctionalxSWT(Shell s){
		shell=s;
	}
	
	public DecompositionFunctionalxSWT(){
		display=new Display();
		shell= new Shell(display);
	}
	
	
	//----------------------
	//Methods
	//----------------------
	
	
	
	public void displayWidget(){

		shell.setLayout(new FillLayout(SWT.FILL));

		Button buttonFile1 =  new Button(shell, SWT.PUSH);
		buttonFile1.setText("Open Org. Unit vs Actor Matrix");
		buttonFile1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.matrix"});
				orgUnitVsActorMatrix = dialog.open();
			
			}

		});
		
		Button buttonFile2 =  new Button(shell, SWT.PUSH);
		buttonFile2.setText("Open Actor vs Role Matrix");
		buttonFile2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.matrix"});
				actorVsRoleMatrix = dialog.open();
			}

		});
		
		Button buttonFile3 =  new Button(shell, SWT.PUSH);
		buttonFile2.setText("Open RACI Matrix");
		buttonFile2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.matrix"});
				raciMatrix = dialog.open();
			}

		});
		
		Button buttonFileSave =  new Button(shell, SWT.PUSH);
		buttonFileSave.setText("Save");
		buttonFileSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				dialog.setFilterExtensions(new String [] {"*.matrix"});
				String result = dialog.open();
				if(result!=null){
					if(!result.endsWith(".matrix")){
						fileSave=result+".matrix";
					}
					else{
						fileSave=result;
					}
				}
			
			}

		});
		
		Button buttonAccept =  new Button(shell, SWT.PUSH);
		buttonAccept.setText("Accept");
		buttonAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}

		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public String getFileorgUnitVsActorMatrix() {
		return orgUnitVsActorMatrix;
	}
	
	public String getFileactorVsRoleMatrix() {
		return actorVsRoleMatrix;
	}
	
	public String getRaciMatrix() {
		return raciMatrix;
	}

	public String getFileSave() {
		return fileSave;
	}

}
