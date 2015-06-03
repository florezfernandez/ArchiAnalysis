package co.edu.uniandes.archimate.analysis.chain.utilities.swt;

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

public class MatrixSWT {
	
	
	//-----------------------------
	//Attributes
	//-----------------------------
	private Display display;
	private Shell shell;
	private String file1;
	private String file2;
	private String fileSave;
	private String nClass;
	
	
	//----------------------------
	//Constructors
	//----------------------------
	
	
	public MatrixSWT(Display d){
		if(d!=null){
		display=d;
		shell=new Shell(display);
		}
	}
	
	public MatrixSWT(Shell s){
		shell=s;
	}
	
	public MatrixSWT(){
		display=new Display();
		shell= new Shell(display);
	}
	
	
	//----------------------
	//Methods
	//----------------------
	
	
	
	public void displayWidget(){

		shell.setLayout(new FillLayout(SWT.FILL));

		final Combo combo=new Combo(shell, SWT.READ_ONLY);
		String[] classes={"com.archimatetool.model.impl.AndJunction"
				,"com.archimatetool.model.impl.AccesRelationship"
				,"com.archimatetool.model.impl.AggregationRelationship"
				,"com.archimatetool.model.impl.AssignmentRelationship"
				,"com.archimatetool.model.impl.AssociationRelationship"
				,"com.archimatetool.model.impl.CompositionRelationship"
				,"com.archimatetool.model.impl.FlowRelationship"
				,"com.archimatetool.model.impl.InfluenceRelationship"
				,"com.archimatetool.model.impl.RealisationRelationship"
				,"com.archimatetool.model.impl.SpecialisationRelationship"
				,"com.archimatetool.model.impl.TriggeringRelationship"
				,"com.archimatetool.model.impl.UsedByRelationship"
				};

		combo.setItems(classes);

		Button buttonFile1 =  new Button(shell, SWT.PUSH);
		buttonFile1.setText("Open 1:");
		buttonFile1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.catalog"});
				dialog.setFilterPath("/Users/DavidBermeo/Desktop/");
				file1 = dialog.open();
			
			}

		});
		Button buttonFile2 =  new Button(shell, SWT.PUSH);
		buttonFile2.setText("Open 2:");
		buttonFile2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.catalog"});
				dialog.setFilterPath("/Users/DavidBermeo/Desktop/");
				file2 = dialog.open();
			}

		});
		
		
		Button buttonFileSave =  new Button(shell, SWT.PUSH);
		buttonFileSave.setText("Save:");
		buttonFileSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				dialog.setFilterExtensions(new String [] {"*.matrix"});
				dialog.setFilterPath("/Users/DavidBermeo/Desktop/");
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
				nClass=combo.getText();
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

	public String getFile1() {
		return file1;
	}
	
	public String getFile2() {
		return file2;
	}
	
	public String getFileSave() {
		return fileSave;
	}

	public String getnClass() {
		return nClass;
	}
	
	

}
