import java.util.ArrayList;
import java.util.Arrays;

public class Defun {
	public static void main(String[] args) {
		
		System.out.println("Prueba");
	
		String code = "defun FaCT (n a b c &key f g &optional h) " +
				"(if (<= n 0)" + 
				"1" + 
				"(* n (fact (1- n))))";
		Interpreter reader = new Interpreter();
		String tabString = "\t"; // Indentation Style
		ArrayList<String> parts = reader.separate(code, 5);
		System.out.println(parts); // borrar
		
		// Start of the function
		String function = "\n\n" + tabString + "public static ReturnValue "+ parts.get(0).toLowerCase() + "(";
		parts.remove(0); // removes the name of the function from the ArrayList
		tabString += "\t";
		ArrayList<String> arguments = unpack(parts.get(0));
		System.out.println(parts); // borrar
		arguments.removeIf(n -> n.equals("&key")); // erases '&key' for Lisp mandatory assign values
		arguments.removeIf(n -> n.equals("&optional")); // erases '&optional' for Lisp optional values
		System.out.println(arguments); // borrar
		
		// Writes down parameter values
		for (String argument: arguments) {
			function += "ReturnValue " + argument + " ";
		}
		function += "){\n";
		parts.remove(0); // Removes the arguments of the function
		System.out.println(parts); // borrar
		
		//Check for Operators to translate easy to define functions
		ArrayList<String> operators = new ArrayList<>(Arrays.asList(
				"+", "-", "*", "/", "mod", "rem", "incf", "decf", //Arithmetic Operations
				"=", "/=", ">", "<", ">=", "<=", "max", "min", "equal", "greater", "less", // Comparison Operations
				"and", "or", "not", "xor", //Logical Operations on Boolean Values
				"setq", "atom", "list", "setf", "cond")); // S-Expressions
		for(String i: parts) {
			ArrayList<String> instruction = unpack(i);
			System.out.println(instruction); //Borrar
			if (operators.contains(instruction.get(0))){ //finds if argument is a common operator
				function += tabString + reader.translate(i.substring(1, i.length()-1)) + ";\n";
			} else if (instruction.get(0).toLowerCase().equals("if")) {
				function += tabString + "if (" + reader.translate(instruction.get(1).substring(1, instruction.get(1).length()-1)) + ") {\n";
				tabString += "\t";
				function += tabString + instruction.get(2) + ";\n";
				tabString = tabString.substring(0, tabString.length() - 1);
				function += tabString + "} else {\n";
				tabString += "\t";
				function += tabString + reader.translate(instruction.get(3)) + ";\n";
				tabString = tabString.substring(0, tabString.length() - 1);
				function += tabString + "}";
				tabString = tabString.substring(0, tabString.length() - 1);
			} else {
				;
			}
		}
		function += "\n" + tabString + "}";
		System.out.println(function); // borrar
	}
	
	private static ArrayList<String> unpack(String code) {
		code = code.substring(1, code.length()-1);
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
}