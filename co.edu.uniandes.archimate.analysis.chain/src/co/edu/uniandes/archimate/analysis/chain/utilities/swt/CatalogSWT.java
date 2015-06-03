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

public class CatalogSWT {


	//-----------------------------
	//Attributes
	//-----------------------------
	private Display display;
	private Shell shell;
	private String file;
	private String nClass;

	//----------------------------
	//Constructors
	//----------------------------


	public CatalogSWT(Display d){
		if(d!=null){
			display=d;
			shell=new Shell(display);
		}
	}

	public CatalogSWT(Shell s){
		shell=s;
	}

	public CatalogSWT(){
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
				,"com.archimatetool.model.impl.ApplicationComponent"
				,"com.archimatetool.model.impl.ApplicationFunction"
				,"com.archimatetool.model.impl.ApplicationInteraction"
				,"com.archimatetool.model.impl.ApplicationInterface"
				,"com.archimatetool.model.impl.ApplicationService"
				,"com.archimatetool.model.impl.Artifact"
				,"com.archimatetool.model.impl.Assessment"
				,"com.archimatetool.model.impl.Bounds"
				,"com.archimatetool.model.impl.BusinessActivity"
				,"com.archimatetool.model.impl.BusinessActor"
				,"com.archimatetool.model.impl.BusinessCollaboration"
				,"com.archimatetool.model.impl.BusinessEvent"
				,"com.archimatetool.model.impl.BusinessFunction"
				,"com.archimatetool.model.impl.BusinessInteraction"
				,"com.archimatetool.model.impl.BusinessInterface"
				,"com.archimatetool.model.impl.BusinessObject"
				,"com.archimatetool.model.impl.BusinessProcess"
				,"com.archimatetool.model.impl.BusinessRole"
				,"com.archimatetool.model.impl.BusinessService"
				,"com.archimatetool.model.impl.CommunicationPath"
				,"com.archimatetool.model.impl.Constraint"
				,"com.archimatetool.model.impl.Contract"
				,"com.archimatetool.model.impl.DataObject"
				,"com.archimatetool.model.impl.Deliverable"
				,"com.archimatetool.model.impl.Device"
				,"com.archimatetool.model.impl.Driver"
				,"com.archimatetool.model.impl.Folder"
				,"com.archimatetool.model.impl.Gap"
				,"com.archimatetool.model.impl.Goal"
				,"com.archimatetool.model.impl.ImplementationMigrationElement"
				,"com.archimatetool.model.impl.InfraestructureFunction"
				,"com.archimatetool.model.impl.InfraestructureInterface"
				,"com.archimatetool.model.impl.InfraestructureService"
				,"com.archimatetool.model.impl.Junction"};

		combo.setItems(classes);

		Button buttonFile =  new Button(shell, SWT.PUSH);
		buttonFile.setText("Save:");
		buttonFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				dialog.setFilterPath("/Users/DavidBermeo/Desktop/");
				String result = dialog.open();
				if(result!=null){
					if(!result.endsWith(".catalog")){
						file=result+".catalog";
					}
					else{
						file=result;
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

	public String getFile() {
		return file;
	}

	public String getnClass() {
		return nClass;
	}
	
	


}
