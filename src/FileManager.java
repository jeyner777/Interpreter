import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class FileManager {
	
	private Interpreter interpreter;
	private String dir;
	
	public FileManager(String dir) {
		interpreter = new Interpreter();
		this.dir = dir;
	}
	
	public String readFile() throws Exception {
		//Se lee cada linea del archivo y se guardan en un String.
		StringBuilder lisp = new StringBuilder("");
		try {
			FileReader fr = new FileReader(dir+"/src/LispFile.lisp");
			BufferedReader entrada = new BufferedReader(fr);
			String s;
			while ((s = entrada.readLine()) != null)
				lisp.append(s).append(" ");
			entrada.close();
		} catch (java.io.FileNotFoundException fnfex) {
			System.err.println("Archivo no encontrado: " + fnfex);
			throw fnfex;
		} catch (java.io.IOException ioex) {
			System.err.println("Error en lectura del archivo: " + ioex);
			throw ioex;
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
					throw new Exception("Error de sintaxis: usos de parntesis incorrecto.");
				} else if (lisp_string.charAt(i) == '\''){
					range[0] = i;
				} else if (lisp_string.charAt(i) != ' ') {
					throw new Exception("Error de sintaxis: caracter incorrecto fuera de parentesis");
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
			throw new Exception("Error de sintaxis: uso de parentesis incorrectos");
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
		
		String java = "import java.util.ArrayList;\n"
				+ "import java.util.Hashtable;\n\n"
				+ "public class JavaFile {\n"
				+ "\tprivate static Hashtable<String, ReturnValue> variables = new Hashtable<>();\n";
		
		//Se agrega el string de las funciones al string java.
		for(String function : functions) {
			try {
				java += interpreter.defun(function);
			} catch(Exception e) {
				throw e;
			}
		}
		
		//Se agregan las demas expresiones en el main del string java.
		java += "\n\tpublic static void main(String[] args) {\n"
				+ "\n\t\tvariables.put(\"t\", new ReturnValue(true));"
				+ "\n\t\tvariables.put(\"nil\", new ReturnValue(false));";
		
		for(String expression : main_expressions) {
			try {
				if(interpreter.isCond(expression)) {
					java += "\n" + interpreter.cond(expression.substring(1, expression.length()-1), 2);
				} else {
					java += "\n\t\t" + interpreter.translate(expression) + ";";
				}
			} catch (Exception e) {
				throw e;
			}
		}
		java += "\n\t}\n";
		
		ArrayList<String> conds = interpreter.getConds();
		for(String cond : conds) {
			java += "\n\t" + cond + "\n";
		}
		
		java += "}";
		return java;
	}
	
	public void writeFile(String java) {
		try {
			FileWriter fw = new FileWriter(dir+"/src/JavaFile.java");
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salida = new PrintWriter(bw);
			salida.println(java);
			salida.close();
		} catch(java.io.IOException ioex) {
			System.err.println("Error en lectura del archivo: " + ioex);
		}
	}
	
	/**
	 * Compiles and run the java file
	 * https://www.journaldev.com/937/compile-run-java-program-another-java-program
	 * @param dir: String
	 */
	public void compileRunProgram() {
		String separator = File.separator;
		//System.out.println("File Serapartor = "+separator);

		separator = System.getProperty("file.separator");
		//System.out.println("File Serapartor = "+separator);

		try {
			runProcess("javac -cp src src"+separator+"JavaFile.java");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("**********");
		try {
			runProcess("java -cp src JavaFile");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Print lines
	 * https://www.journaldev.com/937/compile-run-java-program-another-java-program
	 * @param cmd: String
	 * @param ins: InputStream
	 * @throws Exception
	 */
	private static void printLines(String cmd, InputStream ins) throws Exception {
       String line = null;
       BufferedReader in = new BufferedReader(
    		   new InputStreamReader(ins));
       while ((line = in.readLine()) != null) {
           //System.out.println(cmd + " " + line);
           System.out.println(line);
       }
	}
	
	/**
	 * Run process
	 * https://www.journaldev.com/937/compile-run-java-program-another-java-program
	 * @param command: String
	 * @throws Exception
	 */
	private static void runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        //System.out.println(command + " exitValue() " + pro.exitValue());
    }
}
