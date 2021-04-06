import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {
		String dir = Paths.get(".").toAbsolutePath().normalize().toString();
		FileManager file_manager = new FileManager(dir);	
		String java_syntax;
		try {
			java_syntax = file_manager.readFile();
			file_manager.writeFile(java_syntax);
			file_manager.compileRunProgram();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
