package co.edu.uniandes.archimate.analysis.chain.loadChain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import co.edu.uniandes.archimate.analysis.chain.utilities.ChainedFunction;



public class XmlChainHandler extends DefaultHandler {

	private List<ChainedFunction> functionList = null;
	private ChainedFunction function = null;
	private HashMap<String, String> outputsHash;
	private ArrayList<String> inputs;
	private ArrayList<String> outputsNames;
	
	

	public List<ChainedFunction> getFuncList() {
		return functionList;
	}


	boolean bName =false;
	boolean bClass =false;
	boolean bInputs =false;
	boolean bInput =false;
	boolean bOutputs =false;
	boolean bOutput =false;
	boolean bValue =false;
	boolean bReferenceId =false;
	boolean bReferencedId =false;
	


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {

		if (qName.equalsIgnoreCase("function")) {
			//create a new Function and put it in Map
			function=new ChainedFunction();
			//initialize lists
			inputs=new ArrayList<String>();
			outputsNames=new ArrayList<String>();
			if (functionList == null)
				functionList = new ArrayList<ChainedFunction>();
			if (outputsHash == null)
				outputsHash=new HashMap<String,String>();
			
		} 
		else if (qName.equalsIgnoreCase("class")) {
			//set boolean values for fields, will be used in setting Employee variables
			bClass = true;
		} 
		else if (qName.equalsIgnoreCase("inputs")) {
			bInputs = true;
		} 
		else if (qName.equalsIgnoreCase("input")) {
			bInput = true;
		} 
		else if (qName.equalsIgnoreCase("outputs")) {
			bOutputs = true;
		}
		else if (qName.equalsIgnoreCase("output")) {
            bOutput = true;
        }
		else if (qName.equalsIgnoreCase("value")) {
            bValue = true;
        }
		
		else if (qName.equalsIgnoreCase("referenceId")) {
			bReferenceId = true;
        }
		else if (qName.equalsIgnoreCase("referencedId")) {
			bReferencedId = true;
        }
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("function")) {
			functionList.add(function);
			try {
				String[] outs=function.execute();
				for(int i=0;i<outs.length;i++){
					String outName=outputsNames.get(i);
					if(!outName.equals("")){
						outputsHash.put(outName, outs[i]);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else if (qName.equalsIgnoreCase("inputs")) {
			function.setParams(inputs);
			bInputs=false;
		}
		else if (qName.equalsIgnoreCase("input")) {
			bInput=false;
		}
		else if (qName.equalsIgnoreCase("outputs")) {
			bOutputs=false;
		}
		else if (qName.equalsIgnoreCase("output")) {
			bOutput=false;
		}
		
	}


	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		if (bClass) {
			//age element, set Employee age
			function.setFunctionClass(new String(ch, start, length));
			bClass = false;
		}  else if (bInputs&&bInput&&bValue) {
			String val=new String(ch, start, length);
			inputs.add(val);
			bValue=false;
		} else if (bInputs&&bInput&&bReferencedId) {
			String val=new String(ch, start, length);
			inputs.add(outputsHash.get(val));
			bReferencedId=false;
		}
		else if (bOutputs&&bOutput&&bReferenceId) {
			String val=new String(ch, start, length);
			outputsNames.add(val);
			bReferenceId=false;
		}
		
	}
}