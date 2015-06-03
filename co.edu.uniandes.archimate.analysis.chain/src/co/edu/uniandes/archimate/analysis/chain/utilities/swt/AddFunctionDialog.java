package co.edu.uniandes.archimate.analysis.chain.utilities.swt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import swing2swt.layout.BoxLayout;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXException;

import co.edu.uniandes.archimate.analysis.chain.loadChain.XmlChainHandler;
import co.edu.uniandes.archimate.analysis.chain.utilities.ChainedFunction;
import co.edu.uniandes.archimate.analysis.chain.utilities.XmlFunctionsHandler;

public class AddFunctionDialog extends Dialog {

	protected Object result;
	protected Shell shlAddFunction;
	private Combo functionsCombo;
	private List<ChainedFunction> functions;
	private Label lblDescription;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddFunctionDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAddFunction.open();
		shlAddFunction.layout();
		Display display = getParent().getDisplay();
		while (!shlAddFunction.isDisposed()) {
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
		shlAddFunction = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shlAddFunction.setSize(500, 370);
		shlAddFunction.setText("Add Function");
		shlAddFunction.setLayout(new BorderLayout(0, 0));

		functionsCombo = new Combo(shlAddFunction, SWT.NONE);
		functionsCombo.setLayoutData(BorderLayout.CENTER);
		populateCombo();
		functionsCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int i=functionsCombo.getSelectionIndex();
				if(i!=-1){
					lblDescription.setText(functions.get(i).getFunction().getDescription());

				}
			}
		});

		Composite actionsComposite = new Composite(shlAddFunction, SWT.NONE);
		actionsComposite.setLayoutData(BorderLayout.SOUTH);
		actionsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button btnAdd = new Button(actionsComposite, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(functionsCombo.getSelectionIndex()!=-1){
					EditFunctionDialog efd=new EditFunctionDialog(shlAddFunction, SWT.NONE);

					ChainedFunction func=functions.get(functionsCombo.getSelectionIndex());
					result=efd.open(func);
					if(result!=null){
						shlAddFunction.dispose();
					}
				}
			}
		});
		btnAdd.setText("Add");

		Button btnCancel = new Button(actionsComposite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlAddFunction.dispose();
			}
		});
		btnCancel.setText("Cancel");

		lblDescription = new Label(shlAddFunction, SWT.NONE);
		lblDescription.setLayoutData(BorderLayout.NORTH);
		lblDescription.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n");


	}

	private void populateCombo(){
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			XmlFunctionsHandler handler = new XmlFunctionsHandler();
			saxParser.parse(new File("/Users/DavidBermeo/Desktop/functions.xml"), handler);
			functions=handler.getFuncList();
			String[] fNames=new String[functions.size()];
			for(int i=0;i<functions.size();i++){
				fNames[i]=functions.get(i).getName();
			}
			functionsCombo.setItems(fNames);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
