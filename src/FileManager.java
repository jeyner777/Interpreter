import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class FileManager {
	
	private Interpreter interpreter;
	
	public FileManager() {
		interpreter = new Interpreter();
	}
	
	public String readFile(String dir) {
		//Se lee cada linea del archivo y se guardan en un String.
		StringBuilder lisp = new StringBuilder("");
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader entrada = new BufferedReader(fr);
			String s;
			while ((s = entrada.readLine()) != null)
				lisp.append(s).append(" ");
			entrada.close();
		} catch (java.io.FileNotFoundException fnfex) {
			System.err.println("Archivo no encontrado: " + fnfex);
			return "-1";
		} catch (java.io.IOException ioex) {
			System.err.println("Error en lectura del archivo: " + ioex);
			return "-1";
		}
		
		String lisp_string = lisp.toString();
		int count1 = 0, count2 = 0;
		Integer[] range = new Integer[2];
		ArrayList<Integer[]> ranges = new ArrayList<>();
		
		for(int i = 0; i < lisp_string.length(); i++) {
			if(count1 == count2) {
				if(lisp_string.charAt(i) == '(') {
					if(i != 0) {
						if(lisp_string.charAt(i-1) != '\'') {
							range[0] = i;
						}
					} else {
						range[0] = i;
					}
					count1++;
				} else if (lisp_string.charAt(i) == ')') {
					return "-1";
				} else if (lisp_string.charAt(i) == '\''){
					range[0] = i;
				} else if (lisp_string.charAt(i) != ' ') {
					return "-1";
				}
			} else if(lisp_string.charAt(i) == '(') {
				count1++;
			} else if(lisp_string.charAt(i) == ')') {
				count2++;
				if(count1 == count2) {
					range[1] = i;
					ranges.add(range);
					range = new Integer[2];
				}
			}
		}
		
		if(count1 != count2) {
			return "-1";
		}
		
		ArrayList<String> main_expressions = new ArrayList<>();
		ArrayList<String> functions = new ArrayList<>();
		String main_expression;
		//Se reconocen las expresiones principales, y se verifica si son funciones.
		for(Integer[] pair : ranges) {
			main_expression = lisp_string.substring(pair[0], pair[1]+1);
			if(interpreter.isFunction(main_expression)) {
				functions.add(main_expression);
			} else {
				main_expressions.add(main_expression);
			}
		}
		
		String java = "public class JavaFile {\n";
		
		//Se agrega el string de las funciones al string java.
		for(String function : functions) {
			try {
				java += interpreter.defun(function);
			} catch(Exception e) {
				System.err.println("Existe un error en el codigo");
			}
		}
		
		//Se agregan las demas expresiones en el main del string java.
		java += "\tpublic static void main(String[] args) {\n";
		for(String expression : main_expressions) {
			try {
				java += interpreter.translate(expression);
			} catch (Exception e) {
				System.err.println("Existe un error en el codigo");
			}
		}
		
		java += "\n\t}\n}";
		
		return java;
	}
	
	public void writeFile(String dir, String java) {
		String java_file = "public class JavaFile {\n"
				+ "\tpublic static void main(String[] args) {\n"
				+ java
				+ "\t}\n"
				+"}";
		try {
			FileWriter fw = new FileWriter(dir);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salida = new PrintWriter(bw);
			salida.println(java_file);
			salida.close();
		} catch(java.io.IOException ioex) {
			System.err.println("Error en lectura del archivo: " + ioex);
		}
	}
	
	public void runProgram(String dir) {
		
	}
}
