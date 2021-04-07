import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestAdd {
	Interpreter interpreter = new Interpreter();
	@Test
	public void Suma() throws Exception {
		String resultado = interpreter.add("+ 2 3");
		String esperado = "(2d + 3d)";
		assertEquals(resultado, esperado);
	}
}
