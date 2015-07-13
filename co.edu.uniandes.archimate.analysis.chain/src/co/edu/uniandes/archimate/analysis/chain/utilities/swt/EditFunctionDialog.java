package co.edu.uniandes.archimate.analysis.chain.utilities.swt;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import swing2swt.layout.BoxLayout;
import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import co.edu.uniandes.archimate.analysis.chain.utilities.ChainedFunction;

public class EditFunctionDialog extends Dialog {

	protected Object result;
	protected Shell shlEditFunction;
	private Text param1txt;
	private Text text_1;
	private Text text_2;
	private Label lblDescription;
	private ChainedFunction chainFunc;
	
	private Label[] inputLbls;
	private Text[] inputTxts;
	private String[] inputValues;
	
	private Button[] checkBtns;
	private Button[] checkBtns2;
	
	private Label[] outputLbls;
	private Text[] outputTxts;
	private String[] outputValues;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditFunctionDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlEditFunction.open();
		shlEditFunction.layout();
		Display display = getParent().getDisplay();
		while (!shlEditFunction.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	/**
	 * Open the dialog with info of Chained Functions
	 * @return the result
	 */
	public Object open(ChainedFunction function) {
		createContents(function);
		shlEditFunction.open();
		shlEditFunction.layout();
		Display display = getParent().getDisplay();
		while (!shlEditFunction.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlEditFunction = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shlEditFunction.setSize(450, 300);
		shlEditFunction.setText("Edit Function");
		shlEditFunction.setLayout(new BorderLayout(0, 0));

		lblDescription = new Label(shlEditFunction, SWT.NONE);
		lblDescription.setLayoutData(BorderLayout.NORTH);
		lblDescription.setText("Description\n\n\n\n\n\n\n\n\n");

		Composite paramsComposite = new Composite(shlEditFunction, SWT.NONE);
		paramsComposite.setLayoutData(BorderLayout.CENTER);
		paramsComposite.setLayout(new FillLayout(SWT.VERTICAL));

		Group grpInputs = new Group(paramsComposite, SWT.NONE);
		grpInputs.setText("Inputs");
		grpInputs.setLayout(new GridLayout(3, false));

		Label lblParam = new Label(grpInputs, SWT.NONE);
		lblParam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblParam.setText("Param 1");

		param1txt = new Text(grpInputs, SWT.BORDER);
		param1txt.setText("dgsdgsdfgsdfg");
		param1txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnReferenced = new Button(grpInputs, SWT.CHECK);
		btnReferenced.setText("Referenced");

		Group grpOutputs = new Group(paramsComposite, SWT.NONE);
		grpOutputs.setText("Outputs");
		grpOutputs.setLayout(new GridLayout(2, false));

		Label lblNewLabel = new Label(grpOutputs, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("New Label");

		text_1 = new Text(grpOutputs, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(grpOutputs, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("New Label");

		text_2 = new Text(grpOutputs, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite actionsComposite = new Composite(shlEditFunction, SWT.NONE);
		actionsComposite.setLayoutData(BorderLayout.SOUTH);
		actionsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button btnAccept = new Button(actionsComposite, SWT.NONE);
		btnAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAccept.setText("Accept");

		Button btnCancel = new Button(actionsComposite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlEditFunction.dispose();
			}
		});
		btnCancel.setText("Cancel");

	}



	/**
	 * Create contents of the dialog.
	 */
	private void createContents(ChainedFunction function) {

		chainFunc=function;
		String[] inNames=function.getParamNames();
		String[] outNames=function.getOutNames();
		String[] inputs=function.getParams();
		String[] outputs=function.getOutputs();
		inputValues=inputs;
		outputValues=outputs;

		inputLbls=new Label[inNames.length];
		inputTxts=new Text[inNames.length];
		checkBtns=new Button[inNames.length];
		checkBtns2=new Button[inNames.length];
		
		outputLbls=new Label[outNames.length];
		outputTxts=new Text[outNames.length];

		shlEditFunction = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shlEditFunction.setSize(450, 300);
		shlEditFunction.setText("Edit Function");
		shlEditFunction.setLayout(new BorderLayout(0, 0));

		lblDescription = new Label(shlEditFunction, SWT.NONE);
		lblDescription.setLayoutData(BorderLayout.NORTH);
		lblDescription.setText(function.getFunction().getDescription());

		Composite paramsComposite = new Composite(shlEditFunction, SWT.NONE);
		paramsComposite.setLayoutData(BorderLayout.CENTER);
		paramsComposite.setLayout(new FillLayout(SWT.VERTICAL));

		Group grpInputs = new Group(paramsComposite, SWT.NONE);
		grpInputs.setText("Inputs");
		grpInputs.setLayout(new GridLayout(4, false));

		for(int i=0;i<inputs.length;i++){
			inputLbls[i] = new Label(grpInputs, SWT.NONE);
			inputLbls[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			inputLbls[i].setText(inNames[i]);

			inputTxts[i] = new Text(grpInputs, SWT.SINGLE |SWT.BORDER);
			inputTxts[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			if(inputs[i]!=null){
				inputTxts[i].setText(inputs[i]);
			}
			inputTxts[i].addListener(SWT.FocusOut, new Listener() {
			      public void handleEvent(Event e) {
			          setValueInputs((Text)e.widget);
			         }
			       });
			checkBtns[i] = new Button(grpInputs, SWT.CHECK);
			checkBtns[i].setText("Referenced");
			checkBtns[i].setSelection(function.getReferenced(i));

			
			checkBtns2[i] = new Button(grpInputs, SWT.CHECK);
			checkBtns2[i].setText("User Input");


		}
		Group grpOutputs = new Group(paramsComposite, SWT.NONE);
		grpOutputs.setText("Outputs");
		grpOutputs.setLayout(new GridLayout(2, false));

		for(int i=0;i<outNames.length;i++){
			outputLbls[i] = new Label(grpOutputs, SWT.NONE);
			outputLbls[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			outputLbls[i].setText(outNames[i]);

			outputTxts[i] = new Text(grpOutputs,SWT.SINGLE | SWT.BORDER);
			outputTxts[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			if(outputs[i]!=null){
				outputTxts[i].setText(outputs[i]);
			}
			outputTxts[i].addListener(SWT.FocusOut, new Listener() {
			      public void handleEvent(Event e) {
			          setValueOutputs((Text)e.widget);
			         }
			       });
		}

		Composite actionsComposite = new Composite(shlEditFunction, SWT.NONE);
		actionsComposite.setLayoutData(BorderLayout.SOUTH);
		actionsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button btnAccept = new Button(actionsComposite, SWT.NONE);
		btnAccept.setText("Accept");
		btnAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((Button)e.widget).forceFocus();
				ArrayList<String> nInputs=new ArrayList<String>();
				String[] nOutputs=new String[outputTxts.length];
				for(int i=0;i<inputTxts.length;i++){
					if(!inputTxts[i].isFocusControl()){
						inputTxts[i].forceFocus();
					}
//					nInputs.add(inputTxts[i].getText());
					nInputs.add(inputValues[i]);
					chainFunc.setReferenced(i, checkBtns[i].getSelection());
				}
				for(int i=0;i<nOutputs.length;i++){
//					nOutputs[i]=outputTxts[i].getText();
					if(!outputTxts[i].isFocusControl()){
						outputTxts[i].forceFocus();
					}
				}
				chainFunc.setOutputs(outputValues);
				chainFunc.setParams(nInputs);
				result=chainFunc;
				shlEditFunction.dispose();
			}
		});
		
		
		    

		Button btnCancel = new Button(actionsComposite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlEditFunction.dispose();
			}
		});
		btnCancel.setText("Cancel");

	}
	
	public void setValueInputs(Text e){
		for(int i=0;i<inputValues.length;i++){
			if(e==inputTxts[i]){
				inputValues[i]=e.getText();
			}
		}
	}
	public void setValueOutputs(Text e){
		for(int i=0;i<outputValues.length;i++){
			if(e==outputTxts[i]){
				outputValues[i]=e.getText();
			}
		}
	}
}
