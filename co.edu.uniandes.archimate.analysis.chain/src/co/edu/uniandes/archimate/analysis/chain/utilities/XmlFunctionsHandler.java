package co.edu.uniandes.archimate.analysis.chain.utilities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import co.edu.uniandes.archimate.analysis.chain.utilities.ChainedFunction;



public class XmlFunctionsHandler extends DefaultHandler {

	private List<ChainedFunction> functionList = null;
	private ChainedFunction function = null;

	
	

	public List<ChainedFunction> getFuncList() {
		return functionList;
	}


	boolean bName =false;
	boolean bHanlder =false;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {

		if (qName.equalsIgnoreCase("function")) {
			//create a new Function and put it in Map
			function=new ChainedFunction();
			//initialize lists
			if (functionList == null)
				functionList = new ArrayList<ChainedFunction>();
		} 
		else if (qName.equalsIgnoreCase("handler")) {
			bHanlder = true;
		}
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("function")) {
			functionList.add(function);
		}
	}


	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		if (bHanlder) {
			//age element, set Employee age
			function.setFunctionClass(new String(ch, start, length));
			bHanlder = false;
		}  else if (bName) {
			function.setName(new String(ch, start, length));
		} 
	}
}