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
		variables.add("nil");
	}
	
	public Interpreter(Hashtable<String, Boolean> functions, String function_name) {
		conds = new ArrayList<String>(); //Se guardan los nombres de las funciones cond.
		variables = new ArrayList<String>(); //Se guardan los nombres de las variables.
		this.functions = functions; //Se guardan los nombres de las funciones definidas, y si tienen retorno o no.
		functions.put(function_name, true);
		//Los valores booleanos se trabajan como variables.
		variables.add("t");
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
	
	public boolean isCond(String code) {
		if(code.charAt(0) == '\'') {
			code = code.substring(2, code.length()-1);
			return false;
		}
		code = code.substring(1, code.length()-1);
		String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
		if(operator.equals("cond")) {
			return true;
		}
		return false;
	}
	
	public String translate(String code) throws Exception {
		try {
			if(code.charAt(0) == '\'') {
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
						case "print": return print(code);
						case "setq":  return setq(code);
						case "atom":  return atom(code);
						case "list":  return list(code);
						case "equal":  return equal(code);
						case "=": return equal(code);
						case ">":  return greater(code);
						case "<":  return less(code);
						case "and": return and(code);
						case "cond":  return cond(code, 1);
						case "quote": return quote(code);
						default: return translate(operator);
					}	
				}
			} else if (variables.contains(code.toLowerCase().trim())) {
				return "variables.get(\""+ code.toLowerCase().trim() + "\")";
			} else if (code.trim().charAt(0) == '"' && code.trim().charAt(code.trim().length()-1) == '"') {
				return code;
			} else {
				try {
					Double d = Double.parseDouble(code);
				} catch (Exception e) {
					throw new Exception("La variable '" + code.toLowerCase().trim() + "' no esta definida.");
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
		boolean double_quotes = false;
		
		//Se encuentran los rangos de posiciones donde estan los parametros en el string
		for(int i = 0; i < code.length(); i++) {
			if(double_quotes) {
				if(code.charAt(i) == '"') {
					range[1] = i;
					ranges.add(range);
					range = new Integer[2];
					double_quotes = false;
				}
			} else {
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
					} else if(code.charAt(i) == '"') {
						range[0] = i;
						double_quotes = true;
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
	
	public String add(String code) throws Exception {
		int word_length = 3;
		if(code.charAt(0) == '+') {
			word_length = 1;
		}
		ArrayList<String> parameters = separate(code, word_length);
		String parameter;
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameter = parameters.get(i);
				if(isReturn(parameter)) {
					if(isReturnValue(parameter)) {
						parameters.set(i, translate(parameters.get(i)) + ".getDouble()");
					} else if (isNumeric(parameter)) {
						parameters.set(i, translate(parameters.get(i)));
					} else {
						throw new Exception("Las operaciones aritmeticas solo pueden ser numericas en Lisp.");
					}
				} else {
					throw new Exception("Las operaciones aritmeticas en Lisp deben tener parametros con retorno numerico.");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0);
		for(int i=1; i<parameters.size(); i++) {
			java_syntax += " + " + parameters.get(i);
		}
		java_syntax += ")";
		return java_syntax;
	}
	
	private String subtract(String code) throws Exception {
		int word_length = 8;
		if(code.charAt(0) == '-') {
			word_length = 1;
		}
		ArrayList<String> parameters = separate(code, word_length);
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameters.set(i, translate(parameters.get(i)));
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0);
		for(int i=1; i<parameters.size(); i++) {
			java_syntax += " - " + parameters.get(i);
		}
		java_syntax += ")";
		return java_syntax;
	}
	
	private String multiply(String code) throws Exception {
		int word_length = 8;
		if(code.charAt(0) == '*') {
			word_length = 1;
		}
		ArrayList<String> parameters = separate(code, word_length);
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameters.set(i, translate(parameters.get(i)));
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0);
		for(int i=1; i<parameters.size(); i++) {
			java_syntax += " * " + parameters.get(i);
		}
		java_syntax += ")";
		return java_syntax;
	}
	
	private String divide(String code) throws Exception {
		int word_length = 6;
		if(code.charAt(0) == '/') {
			word_length = 1;
		}
		ArrayList<String> parameters = separate(code, word_length);
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameters.set(i, translate(parameters.get(i)));
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0);
		for(int i=1; i<parameters.size(); i++) {
			java_syntax += " / " + parameters.get(i);
		}
		java_syntax += ")";
		return java_syntax;
	}
	
	private String quote(String code) {
		code = code.toLowerCase();
		if(code.startsWith("'(quote")) {
			return "\"" + code.substring(8,code.length()-1) + "\"";
		} else if (code.startsWith("quote")) {
			return "\"" + code.substring(6,code.length()) + "\"";
		} else if (code.startsWith("'")){
			return "\"" + code.substring(1) + "\"";
		} else {
			return "\"" + code + "\"";
		}
	}
	
	public String setq(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 4);
		
		//Revisa si tiene la cantidad correcta de parametros
		if(parameters.size() < 2) {
			throw new Exception("La definicion de variables con 'setq' requiere de dos parametros");
		} else if (parameters.size() > 2) {
			throw new Exception("La definicion de variables con 'setq' requiere unicamente de dos parametros");
		}
		
		String variable_name = parameters.get(0).toLowerCase().trim();
		String value = parameters.get(1);
		
		if(variable_name != variable_name.split(" ")[0].split("\\(")[0].toLowerCase()) {
			throw new Exception("Nombre de variable incorrecto al utilizar 'setq'");
		} else if(variable_name.equals("t")) {
			throw new Exception("La variable 't' sesta reservada, no puede utilizarse como nombre de variable con 'setq'");
		} else if(variable_name.equals("nil")) {
			throw new Exception("La variable 'nil' sesta reservada, no puede utilizarse como nombre de variable con 'setq'");
		}
		
		variables.add(variable_name);
		
		if(isReturn(value)) {
			if(isReturnValue(value)) {
				return "variables.put(\"" + variable_name + "\"," + translate(value) + ")";
			} else {
				return "variables.put(\"" + variable_name + "\", new ReturnValue(" + translate(value) + "))";
			}
		} else {
			throw new Exception("Asignacion invalida de la variable " + variable_name + ".");
		}
	}
	
	public String defun(String code) {
		return "";
	}
	
	public String function(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 0);
		String function_name = parameters.get(0), parameter;
		String java_syntax = function_name + "(";
		
		if(parameters.size() >= 2) {
			parameter = parameters.get(1);
			try {
				if(isReturnValue(parameter)) {
					java_syntax += translate(parameter);
				} else if (isReturn(parameter)){
					java_syntax += "new ReturnValue(" + translate(parameter) + ")";
				} else {
					throw new Exception("Parametro invalido al llamar a la funcion " + function_name + ".");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		
		for(int i=2; i<parameters.size(); i++) {
			parameter = parameters.get(i);
			try {
				if(isReturnValue(parameter)) {
					java_syntax += "," + translate(parameter);
				} else if (isReturn(parameter)){
					java_syntax += ", new ReturnValue(" + translate(parameter) + ")";
				} else {
					throw new Exception("Parametro invalido al llamar a la funcion " + function_name + ".");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		
		java_syntax += ")";
		return java_syntax;
	}
	
	private String atom(String code) throws Exception {
		code = code.toLowerCase();
		try {
			if (code.startsWith("atom '")) {
				code = code.substring(6);
				if (code.substring(0).equals("()")) {
					return "true";
				} else if (code.matches("[0-9]+")) {
					return "true";
				} else if (code.matches("[a-zA-z]+")) {
					return "true";
				} else if (code.startsWith("(quote ()")) {
					return "true";
				} else if (code.startsWith("(quote (")) {
					return "false";
				} else if (code.startsWith("(list")) {
					return "false";
				} else if (code.startsWith("(")){
					return "false";
				} else {
					return "true";
				}
			} else if (code.equals("atom nil")) {
				return "true";
			} else if (code.equals("atom t")){
				return "true";
			} else if (code.substring(5).matches("[0-9]+")) {
				return "true";
			} else if (variables.contains(code.substring(5))) { // variables en el programa
				return "true";
			} else if (code.substring(5).equals("()")) {
				return "true";
			} else if (code.substring(5).equals("(cons")) {
				return "false";
			} else if (code.substring(5).startsWith("(quote '")) {
				return "false";
			} else if (code.substring(5).matches("[a-zA-z]+")) {
				throw new Exception ("Variable no definida en LISP :(");
			} else {
				return "true";
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private String list(String code) {
		code = code.toLowerCase();
		ArrayList<String> list = separate(code,4);
		String atomlist = "\"";
		for(String atom:list) {
			atomlist += atom + " ";
		}
		atomlist += "\"";
		return atomlist;
	}
	
	private String write(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 5);
		if(parameters.size() != 1) {
			throw new Exception("La funcion 'write' solo puede tener un parametro.");
		}
		String parameter = parameters.get(0);
		if(isReturn(parameter)) {
			if(isReturnValue(parameter)) {
				return "System.out.print(" + translate(parameters.get(0)) + ".toString())";
			} else {
				return "System.out.print(" + translate(parameters.get(0)) + ")";
			}
		} else {
			throw new Exception("La funcion write debe tener un parametro con retorno para mostrarlo en consola.");
		}
	}
	
	private String print(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 5);
		if(parameters.size() != 1) {
			throw new Exception("La funcion 'print' solo puede tener un parametro.");
		}
		String parameter = parameters.get(0);
		if(isReturn(parameter)) {
			if(isReturnValue(parameter)) {
				return "System.out.println(" + translate(parameters.get(0)) + ".toString())";
			} else {
				return "System.out.println(" + translate(parameters.get(0)) + ")";
			}
		} else {
			throw new Exception("La funcion print debe tener un parametro con retorno para mostrarlo en consola.");
		}
	}
	
	public String equal(String code) throws Exception {
		int word_length = 5;
		if(code.charAt(0) == '=') {
			word_length = 1;
		}
		ArrayList<String> parameters = separate(code, word_length);
		String parameter;
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameter = parameters.get(i);
				if(isReturn(parameter)) {
					if(isReturnValue(parameter)) {
						parameters.set(i, translate(parameters.get(i)) + ".getDouble()");
					} else if (isNumeric(parameter)) {
						parameters.set(i, translate(parameters.get(i)));
					} else {
						throw new Exception("Las operaciones comparativas solo pueden ser numericas en Lisp.");
					}
				} else {
					throw new Exception("Las operaciones comparativas en Lisp deben tener parametros con retorno numerico.");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0) + " == " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " == " + parameters.get(i) + ")";
		}
		
		if(parameters.size() > 2) {
			java_syntax = "(" + java_syntax + ")";
		}
		
		return java_syntax;
	}
	
	public String greater(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 1);
		String parameter;
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameter = parameters.get(i);
				if(isReturn(parameter)) {
					if(isReturnValue(parameter)) {
						parameters.set(i, translate(parameters.get(i)) + ".getDouble()");
					} else if (isNumeric(parameter)) {
						parameters.set(i, translate(parameters.get(i)));
					} else {
						throw new Exception("Las operaciones comparativas solo pueden ser numericas en Lisp.");
					}
				} else {
					throw new Exception("Las operaciones comparativas en Lisp deben tener parametros con retorno numerico.");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0) + " > " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " > " + parameters.get(i) + ")";
		}
		
		if(parameters.size() > 2) {
			java_syntax = "(" + java_syntax + ")";
		}
		
		return java_syntax;
	}
	
	public String less(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 1);
		String parameter;
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameter = parameters.get(i);
				if(isReturn(parameter)) {
					if(isReturnValue(parameter)) {
						parameters.set(i, translate(parameters.get(i)) + ".getDouble()");
					} else if (isNumeric(parameter)) {
						parameters.set(i, translate(parameters.get(i)));
					} else {
						throw new Exception("Las operaciones comparativas solo pueden ser numericas en Lisp.");
					}
				} else {
					throw new Exception("Las operaciones comparativas en Lisp deben tener parametros con retorno numerico.");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0) + " < " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " < " + parameters.get(i) + ")";
		}
		
		if(parameters.size() > 2) {
			java_syntax = "(" + java_syntax + ")";
		}
		
		return java_syntax;
	}
	
	public String and(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 3);
		String parameter;
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameter = parameters.get(i);
				if(isReturn(parameter)) {
					if(isReturnValue(parameter)) {
						parameters.set(i, translate(parameters.get(i)) + ".getBoolean()");
					} else if (isNumeric(parameter)) {
						parameters.set(i, translate(parameters.get(i)));
					} else {
						throw new Exception("Las operaciones logicas solo pueden ser booleanas en Lisp.");
					}
				} else {
					throw new Exception("Las operaciones logicas en Lisp deben tener parametros con retorno booleano.");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0);
		for(int i=1; i<parameters.size(); i++) {
			java_syntax += " && " + parameters.get(i);
		}
		java_syntax += ")";
		return java_syntax;
	}
	
	public String or(String code) throws Exception {
		ArrayList<String> parameters = separate(code, 2);
		String parameter;
		for(int i=0; i<parameters.size(); i++) {
			try {
				parameter = parameters.get(i);
				if(isReturn(parameter)) {
					if(isReturnValue(parameter)) {
						parameters.set(i, translate(parameters.get(i)) + ".getBoolean()");
					} else if (isNumeric(parameter)) {
						parameters.set(i, translate(parameters.get(i)));
					} else {
						throw new Exception("Las operaciones logicas solo pueden ser booleanas en Lisp.");
					}
				} else {
					throw new Exception("Las operaciones logicas en Lisp deben tener parametros con retorno booleano.");
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String java_syntax = "(" + parameters.get(0);
		for(int i=1; i<parameters.size(); i++) {
			java_syntax += " || " + parameters.get(i);
		}
		java_syntax += ")";
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
			java_syntax = "private static ReturnValue " + cond_name + " {\n";
			
			for(int i=0; i<parameters.size(); i++) {
				parameters = separate(code, 4);
				test_actions = separate(parameters.get(i).substring(1, parameters.get(i).length()-1), 0);
				//Se obtiene el tests de la condicion
				test = test_actions.get(0);
				
				//Se verifica si es if o if else.
				if(i == 0) {
					if(isReturn(test)) {
						if(isReturnValue(test)) {
							java_syntax += indent + "\tif(" + translate(test) + ".getBoolean()) {\n";
						} else if(isBoolean(test)) {
							java_syntax += indent + "\tif(" + translate(test) + ") {\n";
						} else {
							throw new Exception("Las condiciones deben retornar un valor booleano.");
						}
					} else {
						throw new Exception("Las condiciones deben retornar un valor booleano.");
					}
				} else {
					if(isReturn(test)) {
						if(isReturnValue(test)) {
							java_syntax += indent + "\telse if(" + translate(test) + ".getBoolean()) {\n";
						} else if(isBoolean(test)) {
							java_syntax += indent + "\telse if(" + translate(test) + ") {\n";
						} else {
							throw new Exception("Las condiciones deben retornar un valor booleano.");
						}
					} else {
						throw new Exception("Las condiciones deben retornar un valor booleano.");
					}
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
									java_syntax += indent + "\t\treturn ";
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
							java_syntax += indent + "\t\treturn ";
							if(isReturnValue(action)) {
								java_syntax += translate(action);
							} else {
								java_syntax += "new ReturnValue(" + translate(action) + ");\n";
							}
						} else {
							java_syntax += indent + "\t\t" + translate(action) + ";\n";
						}
					} catch (Exception e) {
						throw e;
					}
				}
				java_syntax += indent + "\t}\n";
			}
			
			java_syntax += indent + "\treturn new ReturnValue();\n\t}";
			//Se guarda el string de la funcion condicional, pero no se retorna.
			conds.set(pos, java_syntax); 
			
			//Se retorna el nombre generado de la funcion condicional
			return cond_name;
		} else {
			//ArrayList<String> parameters = separate(code, 4);
			//Se obtienene la primera condici[on, con su test y sus acciones.
			//ArrayList<String> test_actions = separate(parameters.get(0).substring(1, parameters.get(0).length()-1), 0);
			//String test = test_actions.get(0);
			if(isReturn(test)) {
				if(isReturnValue(test)) {
					java_syntax = indent + "if(" + translate(test) + ".getBoolean()) {\n";
				} else if(isBoolean(test)) {
					java_syntax = indent + "if(" + translate(test) + ") {\n";
				} else {
					throw new Exception("Las condiciones deben retornar un valor booleano.");
				}
			} else {
				throw new Exception("Las condiciones deben retornar un valor booleano.");
			}
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
				if(isReturn(test)) {
					if(isReturnValue(test)) {
						java_syntax += indent + "else if(" + translate(test) + ".getBoolean()) {\n";
					} else if(isBoolean(test)) {
						java_syntax += indent + "else if(" + translate(test) + ") {\n";
					} else {
						throw new Exception("Las condiciones deben retornar un valor booleano.");
					}
				} else {
					throw new Exception("Las condiciones deben retornar un valor booleano.");
				}
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
		//Se verifica si es uno de los dos metodos que nunca tienen retorno.
		if(operator.equals("setq") || operator.equals("write") || operator.equals("print")) {
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
			return false;
		} else if (code.charAt(0) == '('){
			code = code.substring(1, code.length()-1);
			String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
			if (functions.containsKey(operator) || operator.equals("cond")) {
				return true;
			} else {
				return isReturnValue(operator);
			}
		} else if (variables.contains(code.toLowerCase().trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isNumeric(String code) {
		if(code.charAt(0) == '\'') {
			return false;
		} else if (code.charAt(0) == '('){
			code = code.substring(1, code.length()-1);
			String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
			if (operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/")) {
				return true;
			} else if (operator.equals("add") || operator.equals("subtract") || operator.equals("multiply") || operator.equals("divide")) {
				return true;
			} else {
				return isNumeric(operator);
			}
		} else {
			try {
				Double d = Double.parseDouble(code);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}
	
	private boolean isBoolean(String code) {
		if(code.charAt(0) == '\'') {
			return false;
		} else if (code.charAt(0) == '('){
			code = code.substring(1, code.length()-1);
			String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
			if (operator.equals("equals") || operator.equals("=") || operator.equals(">") || operator.equals("<")) {
				return true;
			} else if (operator.equals("and") || operator.equals("or")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public ArrayList<String> getConds() {
		return conds;
	}
}
