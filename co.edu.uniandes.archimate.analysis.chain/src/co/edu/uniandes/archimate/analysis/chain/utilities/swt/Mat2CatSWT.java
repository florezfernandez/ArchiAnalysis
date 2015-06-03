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

public class Mat2CatSWT {


	//-----------------------------
	//Attributes
	//-----------------------------
	private Display display;
	private Shell shell;
	private String file;
	private String saveFile;

	//----------------------------
	//Constructors
	//----------------------------


	public Mat2CatSWT(Display d){
		if(d!=null){
			display=d;
			shell=new Shell(display);
		}
	}

	public Mat2CatSWT(Shell s){
		shell=s;
	}

	public Mat2CatSWT(){
		display=new Display();
		shell= new Shell(display);
	}


	//----------------------
	//Methods
	//----------------------



	public void displayWidget(){

		shell.setLayout(new FillLayout(SWT.FILL));
		
		Button buttonFile1 =  new Button(shell, SWT.PUSH);
		buttonFile1.setText("Open 1:");
		buttonFile1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.matrix"});
				dialog.setFilterPath("/Users/DavidBermeo/Desktop/");
				file = dialog.open();
			
			}

		});

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
						saveFile=result+".catalog";
					}
					else{
						saveFile=result;
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

	public String getFile() {
		return file;
	}

	public String getSaveFile() {
		return saveFile;
	}
	
	


}
