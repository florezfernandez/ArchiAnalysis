package co.edu.uniandes.archimate.analysis.chain.countMatrix;

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction;
import co.edu.uniandes.archimate.analysis.analysischain.IChainableArchiFunction;
import co.edu.uniandes.archimate.analysis.chain.catalog.CatalogResult;
import co.edu.uniandes.archimate.analysis.chain.matrix.MatrixResult;
import co.edu.uniandes.archimate.analysis.chain.utilities.results.ResultSerializable;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.Mat2CatSWT;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.MatrixSWT;
import co.edu.uniandes.archimate.analysis.entities.ValidationResponse;
import co.edu.uniandes.archimate.analysis.util.DiagramModelUtil;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.impl.AssignmentRelationship;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessFunction;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class CountMatrix extends AbstractArchiAnalysisFunction implements IChainableArchiFunction{
	private List<CountCatalogResult> results;
	private List<MatrixResult> matrixResults;


	private ResultSerializable cat1;
	private File saveFile;

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#getName()
	 */
	@Override
	public String getName(){
		return "Count Matrix";
	}

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#validateModel(co.edu.uniandes.archimate.analysis.entities.ValidationResponse)
	 */
	@Override
	public Object validateModel(ValidationResponse validationResponse) throws Exception{
		Mat2CatSWT mSWT=new Mat2CatSWT(Display.getCurrent());
		mSWT.displayWidget();
		String result1=mSWT.getFile();
		String fSave=mSWT.getSaveFile();
		if(result1==null){
			//Validation error
			return null;
		}
		if(fSave!=null){
			saveFile=new File(fSave);
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
		}
		File file;
		file = new File(result1);
		if(!file.exists()){
			//Validation Error
			return null;
		}
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			cat1 = (ResultSerializable) in.readObject();
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
		List<MatrixResult> listR1=null;
		if(cat1!=null){
			matrixResults=(List<MatrixResult>)cat1.getResultEntries();
		}
		else{
			//Validation Error
			return null;
		}




		results = new ArrayList<CountCatalogResult>();

		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see co.edu.uniandes.archimate.analysis.AbstractArchiAnalysisFunction#executeFunction()
	 */
	@Override
	public Object executeFunction() throws Exception {

		for(MatrixResult res:matrixResults){
			int count=0;
			Object[] array=res.toArray();
			for(int i=1;i<array.length;i++){
				if(array[i].equals("X")){
					count++;
				}
			}
			CountCatalogResult result=new CountCatalogResult(array[0].toString(), count);
			results.add(result);
		}

		return null;
	}




	/**
	 * 
	 */
	@Override
	public Object showResults () throws Exception{
		String[] headers = new String[]{"Element Name","Count"};
		int[] widths = new int[]{200,200};

		ResultSerializable resultSerializable=new ResultSerializable(results, widths, headers);
		if(saveFile!=null){
			try
			{

				FileOutputStream fileOut =new FileOutputStream(saveFile);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(resultSerializable);
				out.close();
				fileOut.close();
			}catch(IOException i)
			{
				i.printStackTrace();
			}
		}

		return createTable(headers, widths, results, true, true);


	}


	/**
	 * 
	 */
	@Override
	public Boolean clearResults() {
		return true;
	}

	@Override
	public String[] displayWidget() {
		Mat2CatSWT mSWT=new Mat2CatSWT(Display.getCurrent());
		mSWT.displayWidget();
		String result1=mSWT.getFile();
		String fSave=mSWT.getSaveFile();

		return new String[]{result1,fSave};
	}

	@Override
	public String getDescription() {
		return "This function returns a catalog counting the ocurrences of a relationship matrix.\n"
				+ "Input parameters:\n"
				+ "1. File path of the relationship matrix\n"
				+ "2. File path where the catalog will be saved\n"
				+ "Output:\n"
				+ "1. File path where the catalog was saved";
	}

	@Override
	public String[] executeFunction(String[] params) throws Exception {
		initializeForChain();
		if(params.length!=2){
			throw new Exception("Wrong number of parameters");
		}
		String result1=params[0];
		String fSave=params[1];
		if(result1==null){
			throw new Exception("Matrix can't be null");
		}
		if(fSave!=null){
			saveFile=new File(fSave);
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
		}
		File file;
		file = new File(result1);
		if(!file.exists()){
			throw new Exception("Matrix file not found");
		}
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			cat1 = (ResultSerializable) in.readObject();
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
		List<MatrixResult> listR1=null;
		if(cat1!=null){
			matrixResults=(List<MatrixResult>)cat1.getResultEntries();
		}
		else{
			//Validation Error
			return null;
		}




		results = new ArrayList<CountCatalogResult>();
		executeFunction();
		save();

		return new String[]{fSave};


	}

	private void save(){
		String[] headers = new String[]{"Element Name","Count"};
		int[] widths = new int[]{200,200};

		ResultSerializable resultSerializable=new ResultSerializable(results, widths, headers);
		if(saveFile!=null){
			try
			{

				FileOutputStream fileOut =new FileOutputStream(saveFile);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(resultSerializable);
				out.close();
				fileOut.close();
			}catch(IOException i)
			{
				i.printStackTrace();
			}
		}

	}

	@Override
	public String[] inNames() {
		return new String[]{"Matrix File path:","Result File path"};
	}

	@Override
	public String[] outNames() {
		return new String[]{"Result catalog path"};
	}
}