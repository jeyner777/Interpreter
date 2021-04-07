import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestSubtract {
	Interpreter interpreter = new Interpreter();
	@Test
	public void Subtract() throws Exception {
		String resultado = interpreter.subtract("- 2 3");
		String esperado = "(2d - 3d)";
		assertEquals(esperado, resultado);
	}
}
