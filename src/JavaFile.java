import java.util.ArrayList;
import java.util.Hashtable;

public class JavaFile {
	private static Hashtable<String, ReturnValue> variables = new Hashtable<>();

	public static void main(String[] args) {

		variables.put("t", new ReturnValue(true));
		variables.put("nil", new ReturnValue(false));
		variables.put("a",variables.get("t"));
		variables.put("b", new ReturnValue(23d));
		if((2d > 3d)) {
			variables.put("b", new ReturnValue(23d));
		}
		else if(variables.get("t").getBoolean()) {
			variables.put("b", new ReturnValue(24d));
		}

		System.out.println((1d + cond1().getDouble()));
		System.out.print("El valor de b es: ");
		variables.put("b", new ReturnValue(1d));
		System.out.println(variables.get("b").toString());
	}

	private static ReturnValue cond1() {
		if(variables.get("nil").getBoolean()) {
			System.out.println("hola");
			return new ReturnValue(2d);
		}
		else if(variables.get("a").getBoolean()) {
			System.out.println("yes");
			return new ReturnValue(3d);
		}
		return new ReturnValue();
	}
}
