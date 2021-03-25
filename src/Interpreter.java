import java.util.ArrayList;
import java.util.Hashtable;

public class Interpreter {
	
	private ArrayList<String> conds;
	private ArrayList<String> variables;
	private Hashtable<String, Boolean> functions;
	
	public Interpreter() {
		conds = new ArrayList<String>(); //Se guardan los nombres de las funciones cond.
		variables = new ArrayList<String>(); //Se guardan los nombres de las variables.
		functions = new Hashtable<>(); //Se guardan los nombres de las funciones definidas, y si tienen retorno o no.
		//Los valores booleanos se trabajan como variables.
		variables.add("t");
		variables.add("T");
		variables.add("NIL");
		variables.add("NIl");
		variables.add("NiL");
		variables.add("nIL");
		variables.add("nIl");
		variables.add("niL");
		variables.add("nil");
	}
	
	public Interpreter(Hashtable<String, Boolean> functions, String function_name) {
		conds = new ArrayList<String>(); //Se guardan los nombres de las funciones cond.
		variables = new ArrayList<String>(); //Se guardan los nombres de las variables.
		this.functions = functions; //Se guardan los nombres de las funciones definidas, y si tienen retorno o no.
		functions.put(function_name, true);
		//Los valores booleanos se trabajan como variables.
		variables.add("t");
		variables.add("T");
		variables.add("NIL");
		variables.add("NIl");
		variables.add("NiL");
		variables.add("nIL");
		variables.add("nIl");
		variables.add("niL");
		variables.add("nil");
	}
	
	public boolean isFunction(String code) {
		if(code.charAt(0) == '\'') {
			code = code.substring(2, code.length()-1);
			return false;
		}
		code = code.substring(1, code.length()-1);
		String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
		if(operator.equals("defun")) {
			return true;
		}
		return false;
	}
	
	public String translate(String code) throws Exception {
		try {
			if(code.charAt(0) == '\'') {
				code = code.substring(2, code.length()-1);
				return quote(code);
			} else if (code.charAt(0) == '('){
				code = code.substring(1, code.length()-1);
				String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
				if (functions.containsKey(operator)) {
					return function(code);
				} else {
					switch(operator) {
						case "+":  return add(code);
						case "add":  return add(code);
						case "-":  return subtract(code);
						case "subtract":  return subtract(code);
						case "*":  return multiply(code);
						case "multiply":  return multiply(code);
						case "/":  return divide(code);
						case "divide":  return divide(code);
						case "setq":  return setq(code);
						case "atom":  return atom(code);
						case "list":  return list(code);
						case "equal":  return equal(code);
						case "=": return equal(code);
						case ">":  return greater(code);
						case "<":  return less(code);
						case "cond":  return cond(code);
						default: return operator;
					}	
				}
			} else if (variables.contains(code)) {
				return code;
			} else {
				try {
					Double d = Double.parseDouble(code);
				} catch (Exception e) {
					throw new Exception("La variable '" + code + "' no esta definida.");
				}
				return code + "d";
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Splits parameters
	 * @param code
	 * @param word_length
	 * @return
	 */
	public ArrayList<String> separate(String code, int word_length) {
		//Se quita la palabra de la funcion del string, para retornar solo parametros
		code = code.substring(word_length, code.length());
		
		int count1 = 0, count2 = 0;
		Integer[] range = new Integer[2];
		ArrayList<Integer[]> ranges = new ArrayList<>();
		
		//Se encuentran los rangos de posiciones donde estan los parametros en el string
		for(int i = 0; i < code.length(); i++) {
			if(count1 == count2) {
				if(code.charAt(i) == '(') {
					if(i != 0) {
						if(code.charAt(i-1) != '\'') {
							range[0] = i;
						}
					} else {
						range[0] = i;
					}
					count1++;
				} else if(code.charAt(i) == '\'') {
					range[0] = i;
				} else if(code.charAt(i) != ' ') { 
					if(i == code.length()-1) {
						if(code.charAt(i-1) != ')' && code.charAt(i-1) != ' ') {
							range[1] = i;
							ranges.add(range);
							range = new Integer[2];
						} else {
							range[0] = i;
							range[1] = i;
							ranges.add(range);
							range = new Integer[2];
						}
					} else if(i == 0) {
						range[0] = i;
						if(code.charAt(i+1) == '(' || code.charAt(i+1) == ' ') {
							range[1] = i;
							ranges.add(range);
							range = new Integer[2];
						}
					} else {
						if(code.charAt(i-1) == ')' || code.charAt(i-1) == ' ') {
							range[0] = i;
						}	
						if(code.charAt(i+1) == '(' || code.charAt(i+1) == ' '){
							range[1] = i;
							ranges.add(range);
							range = new Integer[2];
						}
					}
				}
			} else if(code.charAt(i) == '(') {
				count1++;
			} else if(code.charAt(i) == ')') {
				count2++;
				if(count1 == count2) {
					range[1] = i;
					ranges.add(range);
					range = new Integer[2];
				}
			}
		}
		
		//Se genera un ArrayList para los parametros de la funcion
		ArrayList<String> parameters = new ArrayList<>();
		String parameter;
		for(Integer[] pair : ranges) {
			//Se utiliza los rangos de posiciones encontrados anteriormente
			parameter = code.substring(pair[0], pair[1]+1);
			parameters.add(parameter);
		}
		
		return parameters;
	}
	
	//input: add 45 (* 4 5)     setq variable 34.5
	//output: 45 + (4*5);       double variable = 34.5d;
	
	public String add(String code) {
		return "";
	}
	
	private String subtract(String code) {
		return "";
	}
	
	private String multiply(String code) {
		return "";
	}
	
	private String divide(String code) {
		return "";
	}
	
	private String quote(String code) {
		return "";
	}
	
	private String setq(String code) {
		return "";
	}
	
	public String defun(String code) {
		return "";
	}
	
	private String function(String code) {
		return code;
	}
	
	private String atom(String code) {
		return "";
	}
	
	private String list(String code) {
		return "";
	}
	
	public String equal(String code) throws Exception {
		int word_length = 5;
		if(code.charAt(0) == '=') {
			word_length = 1;
		}
		ArrayList<String> parameters = separate(code, word_length);
		for(String parameter : parameters) {
			if(parameter.charAt(0) == '(') {
				try {
					parameter = translate(parameter);
				} catch (Exception e) {
					throw e;
				}
			}
		}
		String java_syntax = "(" + parameters.get(0) + " == " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " == " + parameters.get(i) + ")";
		}
		return java_syntax;
	}
	
	public String greater(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 1);
		for(String parameter : parameters) {
			if(parameter.charAt(0) == '(') {
				try {
					parameter = translate(parameter);
				} catch (Exception e) {
					throw e;
				}
			}
		}
		String java_syntax = "(" + parameters.get(0) + " > " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " > " + parameters.get(i) + ")";
		}
		return java_syntax;
	}
	
	public String less(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 1);
		for(String parameter : parameters) {
			if(parameter.charAt(0) == '(') {
				try {
					parameter = translate(parameter);
				} catch (Exception e) {
					throw e;
				}
			}
		}
		String java_syntax = "(" + parameters.get(0) + " < " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " < " + parameters.get(i) + ")";
		}
		return java_syntax;
	}
	
	private String cond(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 4);
		String java_syntax = "\tprivate static RetunValue cond" + (conds.size()-1) + "() {\n";
		
		ArrayList<String> test_actions;
		String last_expression, operator;
		boolean exists;
		for(String parameter : parameters) {
			exists = false;
			parameter = parameter.substring(1, parameter.length()-1);
			test_actions = separate(parameter, 0);
			if(parameters.indexOf(parameter) == 0) {
				if(test_actions.get(0).charAt(0) == '(') {
					try {
						java_syntax += "\t\tif(" + translate(test_actions.get(0)) + ") {\n";
					} catch (Exception e) {
						throw e;
					}
				} else if(test_actions.get(0).toLowerCase().equals("t") || test_actions.get(0).toLowerCase().equals("nil")) {
					java_syntax += "\t\tif(" + test_actions.get(0) + ") {\n";
				} else {
					for(String variable : variables) {
						if(variable.equals(test_actions.get(0))) {
							exists = true;
						}
					}
					if(exists) {
						java_syntax += "\t\tif(" + test_actions.get(0) + ") {\n";
					} else {
						throw new Exception("Los tests de las condicionales deben ser booleanos.");
					}
				} 
			} else {
				if(test_actions.get(0).charAt(0) == '(') {
					try {
						java_syntax += " else if(" + translate(test_actions.get(0)) + ") {\n";
					} catch (Exception e) {
						throw e;
					}
				} else if(test_actions.get(0).toLowerCase().equals("t") || test_actions.get(0).toLowerCase().equals("nil")) {
					java_syntax += " telse if(" + test_actions.get(0) + ") {\n";
				} else {
					for(String variable : variables) {
						if(variable.equals(test_actions.get(0))) {
							exists = true;
						}
					}
					if(exists) {
						java_syntax += "\t\tif(" + test_actions.get(0) + ") {\n";
					} else {
						throw new Exception("Los tests de las condicionales deben ser booleanos.");
					}
				} 
			}
			
			if(test_actions.size() == 2) {
				
			} else {
				for(int i=1; i<test_actions.size()-1; i++) {
					if(test_actions.get(0).charAt(0) == '(') {
						operator = test_actions.get(i).split(" ")[0].split("\\(")[0].toLowerCase();
						if(operator.equals("setq") || operator.equals("write")) {
							try {
								java_syntax += "\t\t\t" + translate(test_actions.get(i)) + ";\n";
							} catch (Exception e) {
								throw e;
							}
						}
					}
				}
				last_expression = test_actions.get(test_actions.size()-1);
				operator = last_expression.split(" ")[0].split("\\(")[0].toLowerCase();
				if(operator.equals("setq") || operator.equals("write")) {
					//java_syntax = "\t\t\t" + translate();
				}
			}
		}
		return "cond" + conds.size() + "()";
	}
	
	/**
	 * Verifica si hay retorno en una funcion o en una condicional
	 * @param code: String
	 * @param function_name: String
	 * @return
	 */
	public boolean isReturn(String code, String...function_name) {
		String operator, last_action;
		//Se verifica si es cond, ya que estas pueden tener retorno o no.
		if(code.substring(0, 4).equals("cond")) {
			ArrayList<String> parameters = separate(code, 4), test_action;
			System.out.println(parameters);
			for(String parameter : parameters) {
				parameter = parameter.substring(1, parameter.length()-1);
				test_action = separate(parameter, 0);
				last_action = test_action.get(test_action.size()-1);
				//Se evalua el caso en que el cond tenga la misma funcion de donde fue llamada como ultima accion.
				if(last_action.charAt(0) == '(') {
					last_action = last_action.substring(1, last_action.length()-1);
					operator = last_action.split(" ")[0].split("\\(")[0].toLowerCase();
					System.out.println(operator);
					if(function_name.length > 0) {
						if(!operator.equals(function_name[0]) && isReturn(last_action, function_name[0])) {
							return true;
						}
					} else if(isReturn(last_action)) {
						System.out.println(last_action);
						return true;
					}
				} else {
					return true;
				}
			}
			return false;
		}
	
		operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
		System.out.println(operator);
		//Se verifica si es uno de los tres metodos que nunca tienen retorno.
		if(operator.equals("setq") || operator.equals("write") || operator.equals("write-line")) {
			return false;
		//Si es una funcion definida por el usuaurio, se verifica si esta tiene retorno.
		} else if (function_name.length > 0) {
			if (operator.equals(function_name[0])) {
				return false;
			} else if (functions.containsKey(operator)){
				return functions.get(operator);
			}
		} else if(functions.containsKey(operator)) {
			return functions.get(operator);
		}
	
		//Si no es ningun de las posibles funciones sin retorno
		return true;
	}
}
