package co.edu.uniandes.archimate.analysis.chain.utilities.swt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;

import co.edu.uniandes.archimate.analysis.chain.utilities.ChainedFunction;

public class CreateChainSWT extends Shell {



	private ArrayList<ChainedFunction> functions;

	private Display display;
	private List functionsList;
	private Button btnEditFunction;
	private Button btnDeleteFunction;
	private Button btnCreate;


	/**
	 * Launch the application.
	 * @param args
	 */
	public void displayWidget() {
		try {
			CreateChainSWT shell = new CreateChainSWT(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public CreateChainSWT(Display display) {
		super(display, SWT.SHELL_TRIM);
		functions=new ArrayList<ChainedFunction>();
		this.display=display;
		setLayout(new BorderLayout(10, 10));

		Group grpFunctions = new Group(this, SWT.NONE);
		grpFunctions.setText("Functions");
		grpFunctions.setLayoutData(BorderLayout.CENTER);

		functionsList = new List(grpFunctions, SWT.BORDER | SWT.V_SCROLL);
		functionsList.setBounds(0, 0, 300, 228);

		Composite functionActionsComposite = new Composite(this, SWT.NONE);
		functionActionsComposite.setLayoutData(BorderLayout.EAST);
		functionActionsComposite.setLayout(new GridLayout(1, true));

		Button btnAddFunction = new Button(functionActionsComposite, SWT.NONE);
		btnAddFunction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddFunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddFunctionDialog addDialog=new AddFunctionDialog(getShell(), SWT.NONE);
				ChainedFunction f=(ChainedFunction)addDialog.open();
				if(f!=null){
					functions.add(f);
					refresh();
				}
			}
		});
		btnAddFunction.setText("Add Function");
		
		Button btnAddIf = new Button(functionActionsComposite, SWT.NONE);
		GridData gd_btnAddIf = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAddIf.widthHint = 102;
		btnAddIf.setLayoutData(gd_btnAddIf);
		btnAddIf.setText("Add If");
		
		Button btnAddWhile = new Button(functionActionsComposite, SWT.NONE);
		GridData gd_btnAddWhile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAddWhile.widthHint = 101;
		btnAddWhile.setLayoutData(gd_btnAddWhile);
		btnAddWhile.setText("Add While");

		btnEditFunction = new Button(functionActionsComposite, SWT.NONE);
		btnEditFunction.setEnabled(false);
		btnEditFunction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnEditFunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i=functionsList.getSelectionIndex();
				if(i!=-1){
					EditFunctionDialog efd=new EditFunctionDialog(getShell(), SWT.NONE);
					ChainedFunction func=functions.get(functionsList.getSelectionIndex());
					ChainedFunction result=(ChainedFunction) efd.open(func);
					if(result!=null){
						functions.set(i, result);
					}
				}
			}
		});
		btnEditFunction.setText("Edit");

		btnDeleteFunction = new Button(functionActionsComposite, SWT.NONE);
		btnDeleteFunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i=functionsList.getSelectionIndex();
				functions.remove(i);
				refresh();
			}
		});
		btnDeleteFunction.setEnabled(false);
		btnDeleteFunction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDeleteFunction.setText("Delete");

		Composite chainActionsComposite = new Composite(this, SWT.NONE);
		chainActionsComposite.setLayoutData(BorderLayout.SOUTH);
		chainActionsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button btnCancel = new Button(chainActionsComposite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().dispose();
			}
		});
		btnCancel.setText("Cancel");

		btnCreate = new Button(chainActionsComposite, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String [] {"*.xml"});
				dialog.setFilterPath("/Users/DavidBermeo/Desktop/");
				String result = dialog.open();

				File f=new File (result);
				writeFile(f);
				dispose();
			}
		});
		btnCreate.setEnabled(false);
		btnCreate.setText("Create");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("New Chain");
		setSize(450, 300);
		refresh();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void refresh(){
		if(functions.size()>0){
			String[] names=new String[functions.size()];
			for(int i=0;i<names.length;i++){
				names[i]=functions.get(i).getName();
			}
			functionsList.setItems(names);
			btnCreate.setEnabled(true);
			btnDeleteFunction.setEnabled(true);
			btnEditFunction.setEnabled(true);

		}
		else{
			functionsList.setItems(new String[]{});
			btnCreate.setEnabled(false);
			btnDeleteFunction.setEnabled(false);
			btnEditFunction.setEnabled(false);
		}


	}

	private void writeFile(File f){
		try{
			if(!f.exists()){
				f.createNewFile();
			}
			PrintWriter pw=new PrintWriter(f);
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<chain>");
			for(ChainedFunction func:functions){
				pw.println("<function>");
				pw.print("<class>");
				pw.print(func.getFunction().getClass().getCanonicalName());
				pw.println("</class>");
				pw.println("<inputs>");
				String[] inputs=func.getParams();

				for(int i=0;i<inputs.length;i++){
					pw.println("<input>");
					if(func.getReferenced(i)){
						pw.print("<referencedId>");
						pw.print(inputs[i]);
						pw.println("</referencedId>");
					}
					else{
						pw.print("<value>");
						pw.print(inputs[i]);
						pw.println("</value>");						
					}
					pw.println("</input>");
				}
				pw.println("</inputs>");
				pw.println("<outputs>");
				String[] outputs=func.getOutputs();

				for(int i=0;i<outputs.length;i++){
					pw.println("<output>");
					pw.print("<referenceId>");
					pw.print(outputs[i]);
					pw.println("</referenceId>");
					pw.println("</output>");
				}

				pw.println("</outputs>");
				pw.println("</function>");
			}



			pw.println("</chain>");
			pw.close();
		}
		
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
