
public class Main {

	public static void main(String[] args) {
		//Interpreter interpreter = new Interpreter();
		FileManager file_manager = new FileManager();
		System.out.println(file_manager.readFile("C:\\Users\\osjom\\OneDrive\\Universidad\\Semestre 3\\Algoritmos y Estructuras de Datos\\Proyecto 1\\Interpreter\\src\\LispFile.lisp"));
		
		//Prueba de writeFile
		String java = "\t\tint x = 5;\n"
				+ "\t\tif(x > 4) {\n"
				+ "\t\t\tSystem.out.println(x);\n"
				+ "\t\t}\n";
		file_manager.writeFile("C:\\Users\\osjom\\OneDrive\\Universidad\\Semestre 3\\Algoritmos y Estructuras de Datos\\Proyecto 1\\Interpreter\\src\\JavaFile.java", java);
	}

}
