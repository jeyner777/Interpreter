import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestMultiply {
	Interpreter interpreter = new Interpreter();
	@Test
	public void Multiply() throws Exception {
		String resultado = interpreter.multiply("* 2 3");
		String esperado = "(2d * 3d)";
		assertEquals(esperado, resultado);
	}
}
