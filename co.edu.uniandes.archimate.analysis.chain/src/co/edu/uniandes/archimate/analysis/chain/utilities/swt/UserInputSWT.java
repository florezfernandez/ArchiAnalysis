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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UserInputSWT {


	//-----------------------------
	//Attributes
	//-----------------------------
	private Display display;
	private Shell shell;
	private String infoText;
	private String input;

	//----------------------------
	//Constructors
	//----------------------------


	public UserInputSWT(Display d, String nText){
		if(d!=null){
			display=d;
			shell=new Shell(display);
			infoText=nText;
		}
	}

	public UserInputSWT(Shell s){
		shell=s;
	}

	public UserInputSWT(){
		display=new Display();
		shell= new Shell(display);
	}


	//----------------------
	//Methods
	//----------------------



	public void displayWidget(){

		shell.setSize(450, 300);
		shell.setText("User Input");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setText(infoText);
		
		final Text text = new Text(shell, SWT.BORDER);
		Button buttonAccept =  new Button(shell, SWT.PUSH);
		buttonAccept.setText("Accept");
		buttonAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				input=text.getText();
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

	public String getInput() {
		return input;
	}

	


}
