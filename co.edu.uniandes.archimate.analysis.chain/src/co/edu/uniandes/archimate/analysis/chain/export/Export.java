package co.edu.uniandes.archimate.analysis.chain.export;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbookEditorsHandler;

import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.BusinessFunction;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.ITableResult;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.catalog.Catalog;
import co.edu.uniandes.archimate.analysis.chain.matrix.WElement;
import co.edu.uniandes.archimate.analysis.chain.utilities.results.ResultSerializable;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.CatalogSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;


public class Export extends AbstractArchiAnalysisFunction{

	private File file;

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Export Results";
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

		String result = dialog.open();

		file = new File(result);
		if(!file.exists()){
			throw new FileNotFoundException("File not found");
		}

		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {

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
			String newFilePath= file.getPath();
			newFilePath= newFilePath.replace(".catalog", "");
			newFilePath= newFilePath.replace(".matrix", "");
			newFilePath= newFilePath + ".xls";
			File newFile= new File(newFilePath);
			if(newFile.exists()) 
				newFile.delete();
			newFile.createNewFile();
			newFile.setWritable(true);
			
			Workbook book= new HSSFWorkbook();
			FileOutputStream newFileOutputStream= new FileOutputStream(newFile);
			Sheet sheet= book.createSheet();
			
			Row row= sheet.createRow(0);
			for (int j = 0; j < result.getHeaders().length; j++) {
				Cell cell = row.createCell(j);
                cell.setCellValue(result.getHeaders()[j]);
			}
			for (int i = 0; i < result.getResultEntries().size(); i++) {
				row= sheet.createRow(i+1);
				for (int j = 0; j < result.getHeaders().length; j++) {
					Cell cell = row.createCell(j);
	                cell.setCellValue((String) result.getResultEntries().get(i).toArray()[j]);
				}
			}
			
			book.write(newFileOutputStream);
			newFileOutputStream.close();
			Desktop.getDesktop().open(newFile);
		}
		return null;	
		}

	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{
		return null;
	} 
	/**
	 * 
	 */
	@Override
	public Boolean clearResults() {
		return true;
	}

}