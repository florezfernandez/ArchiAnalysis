package co.edu.uniandes.archimate.analysis.chain.loadChain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import co.edu.uniandes.archimate.analysis.chain.utilities.ChainedFunction;
import co.edu.uniandes.archimate.analysis.chain.utilities.swt.UserInputSWT;



public class XmlChainHandler extends DefaultHandler {

/**
 * Constantes para condiciones
 */
	public final static String GT="GT";
	public final static String LT="LT";
	public final static String GEQ="GEQ";
	public final static String LEQ="LEQ";
	public final static String EQUALS="==";
	public final static String S_EQUALS="equals";
	public final static String IS_NULL="IS_NULL";
	public final static String IS_NOT_NULL="IS_NOT_NULL";


	private List<ChainedFunction> functionList = null;
	private ChainedFunction function = null;
	/**
	 * Hash map donde se guardan los outputs de las funciones ejecutadas
	 */
	private HashMap<String, String> outputsHash;
	
	private ArrayList<String> inputs;
	private ArrayList<String> outputsNames;
	/**
	 * Atributos para guardar la informacion de las condiciones y así poder 
	 * evaluarla
	 */
	private String rhs;
	private String lhs;
	private String relation;
/**
 * Este arreglo tiene el estado de verdad de ejecucion. Cuando el primer 
 * elemento (0) esta en true se debe ejecutar todo lo que se parsea. 
 * Cuando entra a un If/while debe agregar un estado de verdad en la posicion 0
 * (Corriendo las demas estados) cuando se termina el if/while se deba remover 
 * el estado de verdad (Ver funciones startelement/ endElement)
 */
	private ArrayList<Boolean> booleanState;

	public List<ChainedFunction> getFuncList() {
		return functionList;
	}

	/**
	 * Atributos para controlar el flujo del Handler. Activar cuando el parser 
	 * lea el tag inicial, desactivar cuando lea el tag de cierre
	 * 
	 */
	boolean bName =false;
	boolean bClass =false;
	boolean bInputs =false;
	boolean bInput =false;
	boolean bOutputs =false;
	boolean bOutput =false;
	boolean bValue =false;
	boolean bReferenceId =false;
	boolean bReferencedId =false;
	boolean bIf=false;
	boolean bWhile=false;
	boolean bCondition=false;
	boolean bRHS=false;
	boolean bLHS=false;
	boolean bConsequent=false;
	boolean bAlternative=false;
	boolean bStatements=false;
	boolean bRelationship=false;
	boolean bUserInput=false;


	@Override
	/**
	 * Prende los booleans correspondientes para manejar el flujo del parser. 
	 * Ademas inicializa los elementos necesarios por cada caso
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {



		if (qName.equalsIgnoreCase("function")) {
			//create a new Function and put it in array
			function=new ChainedFunction();
			//initialize lists
			inputs=new ArrayList<String>();
			outputsNames=new ArrayList<String>();


		} 
		else if(qName.equalsIgnoreCase("chain")){
			if (functionList == null)
				functionList = new ArrayList<ChainedFunction>();
			if (outputsHash == null)
				outputsHash=new HashMap<String,String>();
			if (booleanState == null)
				booleanState = new ArrayList<Boolean>();
				booleanState.add(true);
		}

		else if (qName.equalsIgnoreCase("class")) {
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

		else if (qName.equalsIgnoreCase("if")) {
			bIf = true;
		}
		else if (qName.equalsIgnoreCase("while")) {
			bWhile = true;
		}
		else if (qName.equalsIgnoreCase("condition")) {
			bCondition = true;
		}
		else if (qName.equalsIgnoreCase("rhs")) {
			bRHS = true;
		}
		else if (qName.equalsIgnoreCase("lhs")) {
			bLHS = true;
		}
		else if (qName.equalsIgnoreCase("consequent")) {
			//Arregla el estado de verdad dependiendo de la condicion 
			booleanState.set(0, booleanState.get(0)&&booleanState.get(1));
			bConsequent = true;
		}
		else if (qName.equalsIgnoreCase("alternative")) {
			//Arregla el estado de verdad dependiendo de la condicion 
			booleanState.set(0, !(booleanState.get(0))&&booleanState.get(1));
			bAlternative = true;
		}
		else if (qName.equalsIgnoreCase("statements")) {
			//Arregla el estado de verdad dependiendo de la condicion 
			booleanState.set(0, booleanState.get(0)&&booleanState.get(1));
			bStatements = true;
		}
		else if (qName.equalsIgnoreCase("relationship")) {
			bRelationship = true;
		}
		else if (qName.equalsIgnoreCase("userInput")) {
			bUserInput = true;
		}
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// Si el estado de verdad actual es true ejecutan las funciones
		if(booleanState.get(0)){
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
			else if (qName.equalsIgnoreCase("if")) {
				booleanState.remove(0);
				bIf = false;
			}
			else if (qName.equalsIgnoreCase("while")) {
				booleanState.remove(0);
				bWhile = false;
			}
			//Evalua la condicion y agrega un estado de verdad
			else if (qName.equalsIgnoreCase("condition")) {
				if(relation.equals(GT)||relation.equals(LT)||relation.equals(GEQ)||relation.equals(LEQ)||relation.equals(EQUALS)){
					int iRHS=Integer.parseInt(rhs);
					int iLHS=Integer.parseInt(lhs);
					if(relation.equals(GT)){
						booleanState.add(0,iRHS>iLHS);
					}
					else if(relation.equals(LT)){
						booleanState.add(0,iRHS<iLHS);
					}
					else if(relation.equals(GEQ)){
						booleanState.add(0,iRHS>=iLHS);
					}
					else if(relation.equals(LEQ)){
						booleanState.add(0,iRHS<=iLHS);
					}
					else if(relation.equals(EQUALS)){
						booleanState.add(0,iRHS==iLHS);
					}
				}
				else if (relation.equals(S_EQUALS)){
					booleanState.add(0,rhs.equals(lhs));
				}
				else if (relation.equals(IS_NULL)){
					booleanState.add(0,rhs==null);
				}
				else if (relation.equals(IS_NOT_NULL)){
					booleanState.add(0,!(rhs==null));
				}
				bCondition = false;
			}
			else if (qName.equalsIgnoreCase("rhs")) {
				bRHS = false;
			}
			else if (qName.equalsIgnoreCase("lhs")) {
				bLHS = false;
			}
			else if (qName.equalsIgnoreCase("consequent")) {
				bConsequent = false;
			}
			else if (qName.equalsIgnoreCase("alternative")) {
				bAlternative = false;
			}
			else if (qName.equalsIgnoreCase("statements")) {
				bStatements = false;
			}
			else if (qName.equalsIgnoreCase("relationship")) {
				bRelationship = false;
			}
		}
		else{
			if (qName.equalsIgnoreCase("function")) {



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
			else if (qName.equalsIgnoreCase("if")) {
				booleanState.remove(0);
				bIf = false;
			}
			else if (qName.equalsIgnoreCase("while")) {
				booleanState.remove(0);
				bWhile = false;
			}
			else if (qName.equalsIgnoreCase("condition")) {
				bCondition = false;
			}
			else if (qName.equalsIgnoreCase("rhs")) {
				bRHS = false;
			}
			else if (qName.equalsIgnoreCase("lhs")) {
				bLHS = false;
			}
			else if (qName.equalsIgnoreCase("consequent")) {
				bConsequent = false;
			}
			else if (qName.equalsIgnoreCase("alternative")) {
				bAlternative = false;
			}
			else if (qName.equalsIgnoreCase("statements")) {
				bStatements = false;
			}
		}

	}


	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if(booleanState.get(0)){
			if (bClass) {
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
			else if (bInputs&&bInput&&bUserInput) {
				String val=new String(ch, start, length);
				String in=askUser(val);
				inputs.add(in);
				bUserInput=false;
			}
			else if (bOutputs&&bOutput&&bReferenceId) {
				String val=new String(ch, start, length);
				outputsNames.add(val);
				bReferenceId=false;
			}
			else if (bRHS&&bValue&&bCondition){
				rhs=new String(ch, start, length);
				bValue = false;
			}
			else if (bLHS&&bValue&&bCondition){
				lhs=new String(ch, start, length);
				bValue = false;
			}
			else if (bRHS&&bReferencedId&&bCondition){
				rhs=outputsHash.get(new String(ch, start, length));
				bReferencedId = false;
			}
			else if (bLHS&&bReferencedId&&bCondition){
				lhs=outputsHash.get(new String(ch, start, length));
				bReferencedId = false;
			}
			else if (bRelationship){
				relation=new String(ch, start, length);
				bRelationship = false;
			}
		}
		else{
			if (bClass) {
				bClass = false;
			}  else if (bValue) {
				bValue=false;
			} else if (bReferencedId) {
				bReferencedId=false;
			}
			else if (bReferenceId) {
				bReferenceId=false;
			}
		}



	}

	private String askUser(String val) {
		UserInputSWT uiswt=new UserInputSWT(Display.getCurrent(), val);
		uiswt.displayWidget();
		return uiswt.getInput();
	}
}