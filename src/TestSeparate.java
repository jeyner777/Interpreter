import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestSeparate {
	Interpreter interpreter = new Interpreter();
	@Test
	public void Separate() throws Exception {
		String resultado = interpreter.separate("cond ((> 2 3)(setq b 23)) ((equal (list 1 2 3) (list 1 2 3)) (setq b 24))", 4).toString();
		String esperado = "[((> 2 3)(setq b 23)), ((equal (list 1 2 3) (list 1 2 3)) (setq b 24))]";
		assertEquals(esperado, resultado);
	}
}
