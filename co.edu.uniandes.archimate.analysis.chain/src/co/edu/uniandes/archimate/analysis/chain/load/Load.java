package co.edu.uniandes.archimate.analysis.chain.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.impl.BusinessFunction;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.chain.catalog.Catalog;
import co.edu.uniandes.archimate.analysis.chain.matrix.WElement;
import co.edu.uniandes.archimate.analysis.chain.utilities.results.ResultSerializable;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CatalogSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;


public class Load extends AbstractArchiAnalysisFunction{


	private File file;


	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Load Results";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{

		

		IWorkbench wb = PlatformUI.getWorkbench(); 
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		
		FileDialog dialog = new FileDialog(win.getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String [] {"*.matrix","*.catalog"});
		dialog.setFilterPath("/Users/DavidBermeo/Desktop/");

		String result = dialog.open();


		file = new File(result);
		if(!file.exists()){
			//Error archivo no existe
		}
		//		}

		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {
		return null;
	}




	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{

		ResultSerializable result = null;
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			result = (ResultSerializable) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException i)
		{
			i.printStackTrace();
		}catch(ClassNotFoundException c)
		{
			System.out.println("class not found");
			c.printStackTrace();
		}

		if(result!=null){
			String[] headers = result.getHeaders();
			int[] widths=result.getColumnSize();
			return createTable(headers, widths, result.getResultEntries(), true, true);

		}
		else{
			return null;
		}


	} 
	/**
	 * 
	 */
	@Override
	public Boolean clearResults() {
		return true;
	}
}