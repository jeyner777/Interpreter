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
						case "write": return write(code);
						case "setq":  return setq(code);
						case "atom":  return atom(code);
						case "list":  return list(code);
						case "equal":  return equal(code);
						case "=": return equal(code);
						case ">":  return greater(code);
						case "<":  return less(code);
						case "cond":  return cond(code, 0);
						default: return operator;
					}	
				}
			} else if (variables.contains(code)) {
				return "variables.get("+ code + ")";
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
	
	public String setq(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 4);
		
		//Revisa si tiene la cantidad correcta de parametros
		if(parameters.size() < 2) {
			throw new Exception("La definicion de variables con 'setq' requiere de dos parametros");
		} else if (parameters.size() > 2) {
			throw new Exception("La definicion de variables con 'setq' requiere unicamente de dos parametros");
		}
		
		String variable_name = parameters.get(0).toLowerCase();
		String value = parameters.get(1);
		
		if(variable_name != variable_name.split(" ")[0].split("\\(")[0].toLowerCase()) {
			throw new Exception("Nombre de variable incorrecto al utilizar 'setq'");
		} else if(variable_name.equals("t")) {
			throw new Exception("La variable 't' sesta reservada, no puede utilizarse como nombre de variable con 'setq'");
		} else if(variable_name.equals("nil")) {
			throw new Exception("La variable 'nil' sesta reservada, no puede utilizarse como nombre de variable con 'setq'");
		}
		
		if(variables.contains(variable_name)) {
			return "variables.replace(" + variable_name + ", " + translate(value) + ")";
		}
		
		variables.add(variable_name);
		
		return "variables.put(" + variable_name + ", new ReturnValue(" + translate(value) + "))";
	}
	
	public String defun(String code) {
		return "";
	}
	
	public String function(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 0);
		String function_name = parameters.get(0);
		String java_syntax = function_name + "(";
		
		if(parameters.size() >= 2) {
			try {
				java_syntax += translate(parameters.get(1));
			} catch (Exception e) {
				throw e;
			}
		}
		
		for(int i=2; i<parameters.size(); i++) {
			try {
				java_syntax += ", " + translate(parameters.get(i));
			} catch (Exception e) {
				throw e;
			}
		}
		
		return java_syntax + ")";
	}
	
	private String atom(String code) {
		return "";
	}
	
	private String list(String code) {
		return "";
	}
	
	private String write(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 5);
		if(parameters.size() != 1) {
			throw new Exception("La funcion 'write' solo puede tener un parametro.");
		}
		return "System.out.println(" + translate(parameters.get(0)) + ")";
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
	
	public String cond(String code, int indentation_level) throws Exception {
		String indent = "";
		for(int i=0; i<indentation_level; i++) {
			indent += "\t";
		}
		
		ArrayList<String> parameters = separate(code, 4);
		//Se obtienene la primera condici[on, con su test y sus acciones.
		ArrayList<String> test_actions = separate(parameters.get(0).substring(1, parameters.get(0).length()-1), 0);
		String test = test_actions.get(0), java_syntax;
		if (isReturn(code)) {
			int pos = conds.size();
			String cond_name = "cond" + (pos+1) + "()";
			conds.add(cond_name);
			java_syntax = "private static ReturnValue() " + cond_name + " {\n";
			
			for(int i=0; i<parameters.size(); i++) {
				parameters = separate(code, 4);
				test_actions = separate(parameters.get(i).substring(1, parameters.get(i).length()-1), 0);
				//Se obtiene el tests de la condicion
				test = test_actions.get(0);
				
				//Se verifica si es if o if else.
				if(i == 0) {
					java_syntax += indent + "if(" + translate(test) + ") {\n";
				} else {
					java_syntax += indent + "else if(" + translate(test) + ") {\n";
				}
				
				//Se recorren todas las acciones de la condicion.
				for(int j=1; j<test_actions.size(); j++) {
					String action = test_actions.get(j), action2;
					if(action.charAt(0) == '(') {
						action2 = action.substring(1, action.length()-1);
						String operator = action2.split(" ")[0].split("\\(")[0].toLowerCase(); 
						if(operator.equals("cond")) {
							try {
								if(j == test_actions.size()-1) {
									java_syntax += indent + "\treturn ";
								}
								java_syntax += cond(action2, indentation_level) + ";\n";
								continue;
							} catch (Exception e) {
								throw e;
							}
						}
					}
					
					try {
						if(j == test_actions.size()-1) {
							java_syntax += indent + "\treturn ";
							if(isReturnValue(action)) {
								java_syntax += translate(action);
							} else {
								java_syntax += "new ReturnValue(" + translate(action) + ");\n";
							}
						} else {
							java_syntax += indent + "\t" + translate(action) + ";\n";
						}
					} catch (Exception e) {
						throw e;
					}
				}
				java_syntax += indent + "}\n";
			}
			
			//Se guarda el string de la funcion condicional, pero no se retorna.
			conds.set(pos, java_syntax); 
			
			//Se retorna el nombre generado de la funcion condicional
			return cond_name;
		} else {
			//ArrayList<String> parameters = separate(code, 4);
			//Se obtienene la primera condici[on, con su test y sus acciones.
			//ArrayList<String> test_actions = separate(parameters.get(0).substring(1, parameters.get(0).length()-1), 0);
			//String test = test_actions.get(0);
			java_syntax = indent + "if(" + translate(test) + ") {\n";
			for(int i=1; i<test_actions.size(); i++) {
				String action = test_actions.get(i), action2;
				if(action.charAt(0) == '(') {
					action2 = action.substring(1, action.length()-1);
					String operator = action2.split(" ")[0].split("\\(")[0].toLowerCase(); 
					if(operator.equals("cond")) {
						try {
							java_syntax += cond(action2, indentation_level+1);
							continue;
						} catch (Exception e) {
							throw e;
						}
					}
				}
				try {
					java_syntax += indent + "\t" + translate(action) + ";\n";
				} catch (Exception e) {
					throw e;
				}
			}
			java_syntax += indent + "}\n";
			
			for(int i=1; i<parameters.size(); i++) {
				parameters = separate(code, 4);
				test_actions = separate(parameters.get(i).substring(1, parameters.get(i).length()-1), 0);
				test = test_actions.get(0);
				java_syntax += indent + "else if(" + translate(test) + ") {\n";
				for(int j=1; j<test_actions.size(); j++) {
					String action = test_actions.get(j), action2;
					if(action.charAt(0) == '(') {
						action2 = action.substring(1, action.length()-1);
						String operator = action2.split(" ")[0].split("\\(")[0].toLowerCase(); 
						if(operator.equals("cond")) {
							try {
								java_syntax += cond(action2, indentation_level+1);
								continue;
							} catch (Exception e) {
								throw e;
							}
						}
					}
					try {
						java_syntax += indent + "\t" + translate(action) + ";\n";
					} catch (Exception e) {
						throw e;
					}
				}
				java_syntax += indent + "}\n";
			}
			
			return java_syntax;
		}
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
		if(code.length() >= 4) {
			if(code.substring(0, 4).equals("cond")) {
				ArrayList<String> parameters = separate(code, 4), test_action;
				for(String parameter : parameters) {
					parameter = parameter.substring(1, parameter.length()-1);
					test_action = separate(parameter, 0);
					last_action = test_action.get(test_action.size()-1);
					//Se evalua el caso en que el cond tenga la misma funcion de donde fue llamada como ultima accion.
					if(last_action.charAt(0) == '(') {
						last_action = last_action.substring(1, last_action.length()-1);
						operator = last_action.split(" ")[0].split("\\(")[0].toLowerCase();
						if(function_name.length > 0) {
							if(!operator.equals(function_name[0]) && isReturn(last_action, function_name[0])) {
								return true;
							}
						} else if(isReturn(last_action)) {
							return true;
						}
					} else {
						return true;
					}
				}
				return false;
			}
		}
	
		operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
		//Se verifica si es uno de los tres metodos que nunca tienen retorno.
		if(operator.equals("setq") || operator.equals("write")) {
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
	
	public boolean isReturnValue(String code) {
		if(code.charAt(0) == '\'') {
			code = code.substring(2, code.length()-1);
			return false;
		} else if (code.charAt(0) == '('){
			code = code.substring(1, code.length()-1);
			String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
			if (functions.containsKey(operator) || operator.equals("cond")) {
				return true;
			} else {
				return false;
			}
		} else if (variables.contains(code)) {
			return true;
		} else {
			try {
				Double d = Double.parseDouble(code);
			} catch (Exception e) {
				return true;
			}
			return false;
		}
	}
	
	public ArrayList<String> getConds() {
		return conds;
	}
}
