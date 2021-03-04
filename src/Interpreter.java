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
	
	private String equal(String code) {
		return "";
	}
	
	private String greater(String code) {
		return "";
	}
	
	private String less(String code) {
		return "";
	}
	
	private String cond(String code) {
		return "";
	}
}
