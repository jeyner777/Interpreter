import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestEqual {
	Interpreter interpreter = new Interpreter();
	@Test
	public void Equal() throws Exception {
		String resultado = interpreter.equal("= 2 3");
		String esperado = "(2d == 3d)";
		assertEquals(esperado, resultado);
	}
}
