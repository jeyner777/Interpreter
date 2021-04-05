import java.util.ArrayList;
import java.util.Arrays;

public class Defun {
	public static void main(String[] args) throws Exception {
		
		System.out.println("Prueba");
	
		// Script de main
		String code = "defun sqr (x) (* x x)";
		String code2 = "defun FaCT (z a b c &key f g &optional h) " +
				"(if (<= n 0)" + 
				"1" + 
				"(* n (fact (1- n))))";
		Interpreter reader = new Interpreter();
		ArrayList<String> parts = reader.separate(code, 5);
		System.out.println(parts); // borrar
		
		// 1  a 9
		// llamar a translate si cond, setq write
		// 10
		// averiguar y si SI es Return --> translate y cambiar que NO void sino RValue
		// luego retornar "return ReturnValue(interpreter interno .translate(linea_de_codigo))
		// si NO es return solo translate
		
		// Start of the function
		String tabString = "\t"; // Indentation Style
		String function = "\n\n" + tabString + "public static ReturnValue "+ parts.get(0).toLowerCase() + "(";
		parts.remove(0); // removes the name of the function from the ArrayList
		tabString += "\t";
		ArrayList<String> arguments = new ArrayList<>();
		
		//Check if only one or more parameters on the function
		if(parts.get(0).length() <= 3 ) {
			arguments.add(Character.toString(parts.get(0).charAt(1)));
		} else {
			arguments = reader.separate(parts.get(0).substring(1, parts.get(0).length()-1),0);
		}
		
		arguments.removeIf(n -> n.equals("&key")); // erases '&key' for Lisp mandatory assign values
		arguments.removeIf(n -> n.equals("&optional")); // erases '&optional' for Lisp optional values
		
		// Writes down parameter values
		for (String argument: arguments) {
			function += "ReturnValue " + argument + " ";
		}
		function += "){\n";
		parts.remove(0); // Removes the arguments of the function
		
		System.out.println(function); // borrar
		System.out.println(parts); // borrar
		
		//Check for Operators to translate easy to define functions
		ArrayList<String> operators = new ArrayList<>(Arrays.asList(
				"+", "-", "*", "/", "mod", "rem", "incf", "decf", //Arithmetic Operations
				"=", "/=", ">", "<", ">=", "<=", "max", "min", "equal", "greater", "less", // Comparison Operations
				"and", "or", "not", "xor", //Logical Operations on Boolean Values
				"setq", "atom", "list", "setf", "cond")); // S-Expressions
		
		//Determine type of function
		//1. Es un ReturnValue, una variable o una funcion con retorno.
		//2. Es un retorno normal, como una suma o resta 
		//3. No tiene retorno, comp si es un print o un setq
		
		String value = parts.get(parts.size()-1);
		if(reader.isReturn(value)) {
			for(int i =0; i < parts.size()-2; i++) {
				function += reader.translate(parts.get(i));
			}
			function += tabString + reader.translate(parts.get(parts.size()-1)) + ";\n" ;
		} else if(reader.isReturnValue(value)) {
			for(int i =0; i < parts.size()-2; i++) {
				function += reader.translate(parts.get(i));
			}
			function += tabString + reader.translate(parts.get(parts.size()-1)) + ";\n" ;
		} else {
			for(int i =0; i < parts.size()-2; i++) {
				function += reader.translate(parts.get(i));
			}
			function += tabString + reader.translate(parts.get(parts.size()-1)) + ";\n" ;
		}
		
		System.out.println(function);

		// aqui seria de volarme esto
		/*
		for(String i: parts) {
			ArrayList<String> instruction = reader.separate(i.substring(1, i.length()-1),0);
			System.out.println(instruction); //Borrar
			if(parts.indexOf(i) < parts.size()-1) {
				if (operators.contains(instruction.get(0))){ //finds if argument is a common operator
					function += tabString + reader.translate(i.substring(1, i.length()-1)) + ";\n";
				} else {
					function += tabString + reader.translate(i.substring(1, i.length()-1)) + ";\n";
					;
				}
			} else {
				function += tabString + reader.translate(i.substring(1, i.length()-1)) + ";\n";	
			}
		}
		*/
		
		function += "\n" + tabString + "}";
		System.out.println(function); // borrar
	}
	
	/* 
	 * else if (instruction.get(0).toLowerCase().equals("if")) {
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
				}
	 */
}