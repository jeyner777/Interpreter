import java.util.ArrayList;
import java.util.Hashtable;

public class JavaFile {
	private static Hashtable<String, ReturnValue> variables = new Hashtable<>();


	public static ReturnValue igual(ReturnValue x, ReturnValue y){

		return new ReturnValue(x.toString().equals(y.toString()));
	}

	public static void main(String[] args) {

		variables.put("t", new ReturnValue(true));
		variables.put("nil", new ReturnValue(false));
		variables.put("a",variables.get("t"));
		variables.put("b", new ReturnValue(23d));
		if((2d > 3d)) {
			variables.put("b", new ReturnValue(23d));
		}
		else if("1 2 3 ".equals("1 2 3 ")) {
			variables.put("b", new ReturnValue(24d));
		}

		System.out.println((1d + cond1().getDouble()));
		System.out.print("El valor de b es: ");
		variables.put("b", new ReturnValue(true));
		System.out.println(igual(new ReturnValue("2s"), new ReturnValue("2s")).toString());
	}

	private static ReturnValue cond1() {
		if(variables.get("nil").getBoolean()) {
			System.out.println("prueba");
			return new ReturnValue(2d);
		}
		else if(variables.get("a").getBoolean()) {
			System.out.println("yes");
			return new ReturnValue(3d);
		}
		return new ReturnValue();
	}
}
