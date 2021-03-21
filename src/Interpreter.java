import java.util.ArrayList;

public class Interpreter {
	
	public Interpreter() {
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
	
	public String translate(String code) {
		if(code.charAt(0) == '\'') {
			code = code.substring(2, code.length()-1);
			return quote(code);
		} else {
			code = code.substring(1, code.length()-1);
			String operator = code.split(" ")[0].split("\\(")[0].toLowerCase();
			switch(operator) {
				case "+":  return add(code);
				case "-":  return subtract(code);
				case "*":  return multiply(code);
				case "/":  return divide(code);
				case "setq":  return setq(code);
				case "atom":  return atom(code);
				case "list":  return list(code);
				case "equal":  return equal(code);
				case "greater":  return greater(code);
				case "less":  return less(code);
				case "cond":  return cond(code);
				default: return "";
			}
		}
	}
	
	private ArrayList<String> separate(String code, int word_length) {
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
	
	private String add(String code) {
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
	
	private String atom(String code) {
		return "";
	}
	
	private String list(String code) {
		return "";
	}
	
	public String equal(String code) {
		int word_length = 5;
		if(code.charAt(0) == '=') {
			word_length = 1;
		}
		ArrayList<String> parameters = separate(code, word_length);
		for(String parameter : parameters) {
			if(parameter.charAt(0) == '(') {
				parameter = translate(parameter);
			}
		}
		String java_syntax = "(" + parameters.get(0) + " == " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " == " + parameters.get(i) + ")";
		}
		return java_syntax;
	}
	
	public String greater(String code) {
		ArrayList<String> parameters = separate(code, 1);
		for(String parameter : parameters) {
			if(parameter.charAt(0) == '(') {
				parameter = translate(parameter);
			}
		}
		String java_syntax = "(" + parameters.get(0) + " > " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " > " + parameters.get(i) + ")";
		}
		return java_syntax;
	}
	
	public String less(String code) {
		ArrayList<String> parameters = separate(code, 1);
		for(String parameter : parameters) {
			if(parameter.charAt(0) == '(') {
				parameter = translate(parameter);
			}
		}
		String java_syntax = "(" + parameters.get(0) + " < " + parameters.get(1) + ")";
		for(int i=2; i<parameters.size(); i++) {
			java_syntax += " && (" + parameters.get(0) + " < " + parameters.get(i) + ")";
		}
		return java_syntax;
	}
	
	private String cond(String code) {
		return "";
	}
}
