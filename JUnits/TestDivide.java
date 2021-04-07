import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestDivide {
	Interpreter interpreter = new Interpreter();
	@Test
	public void Divide() throws Exception {
		String resultado = interpreter.divide("/ 2 3");
		String esperado = "(2d / 3d)";
		assertEquals(esperado, resultado);
	}
}
