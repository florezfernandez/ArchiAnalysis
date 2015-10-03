package co.edu.uniandes.archimate.analysis.chain.utilities.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FilterModelSWT {


	//-----------------------------
	//Attributes
	//-----------------------------
	private Display display;
	private Shell shell;
	private String elements;

	//----------------------------
	//Constructors
	//----------------------------


	public FilterModelSWT(Display d){
		if(d!=null){
			display=d;
			shell=new Shell(display);
		}
	}

	public FilterModelSWT(Shell s){
		shell=s;
	}

	public FilterModelSWT(){
		display=new Display();
		shell= new Shell(display);
	}


	//----------------------
	//Methods
	//----------------------



	public void displayWidget(){

		shell.setLayout(new FillLayout(SWT.FILL));

		Group attributeGroup = new Group(shell, SWT.NONE);
		attributeGroup.setLayout(new GridLayout(2, false));
		
		Label lblAttribute = new Label(attributeGroup, SWT.NONE);
		lblAttribute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblAttribute.setText("Elements");
		
		final Text textElements = new Text(attributeGroup, SWT.BORDER);
		textElements.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group buttonGroup = new Group(shell, SWT.NONE);
		buttonGroup.setLayout(new GridLayout(1, false));
		
		Button buttonAccept =  new Button(buttonGroup, SWT.PUSH);
		buttonAccept.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		buttonAccept.setText("Accept");
		buttonAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				elements= textElements.getText();
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

	public String getElements() {
		return elements;
	}


}
