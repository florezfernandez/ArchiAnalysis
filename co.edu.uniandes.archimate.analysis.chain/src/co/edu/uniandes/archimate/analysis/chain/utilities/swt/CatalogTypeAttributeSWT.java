package co.edu.uniandes.archimate.analysis.chain.utilities.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import swing2swt.layout.BorderLayout;

public class CatalogTypeAttributeSWT {


	//-----------------------------
	//Attributes
	//-----------------------------
	private Display display;
	private Shell shell;
	private String file;
	private String nClass;
	private String type;
	private String attributes;

	//----------------------------
	//Constructors
	//----------------------------


	public CatalogTypeAttributeSWT(Display d){
		if(d!=null){
			display=d;
			shell=new Shell(display);
		}
	}

	public CatalogTypeAttributeSWT(Shell s){
		shell=s;
	}

	public CatalogTypeAttributeSWT(){
		display=new Display();
		shell= new Shell(display);
	}


	//----------------------
	//Methods
	//----------------------



	public void displayWidget(){

		shell.setLayout(new FillLayout(SWT.FILL));
		
		Group comboGroup = new Group(shell, SWT.NONE);
		comboGroup.setLayout(new GridLayout(1, false));
		
		final Combo combo=new Combo(comboGroup, SWT.READ_ONLY);
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

		Group propertyGroup = new Group(shell, SWT.NONE);
		propertyGroup.setLayout(new GridLayout(2, false));
		
		Label lblPropValue = new Label(propertyGroup, SWT.NONE);
		lblPropValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblPropValue.setText("Type");
		
		final Text textPropValue = new Text(propertyGroup, SWT.BORDER);
		textPropValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group attributeGroup = new Group(shell, SWT.NONE);
		attributeGroup.setLayout(new GridLayout(2, false));
		
		Label lblAttribute = new Label(attributeGroup, SWT.NONE);
		lblAttribute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblAttribute.setText("Attributes");
		
		final Text textAttributes = new Text(attributeGroup, SWT.BORDER);
		textAttributes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group buttonGroup = new Group(shell, SWT.NONE);
		buttonGroup.setLayout(new GridLayout(1, false));
		
		Button buttonFile =  new Button(buttonGroup, SWT.PUSH);
		buttonFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		buttonFile.setText("Save:");
		buttonFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
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
		Button buttonAccept =  new Button(buttonGroup, SWT.PUSH);
		buttonAccept.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		buttonAccept.setText("Accept");
		buttonAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nClass=combo.getText();
				type= textPropValue.getText();
				attributes= textAttributes.getText();
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
	
	public String getPropertyValue(){
		return type;
	}

	public String getAttributes() {
		return attributes;
	}


}
