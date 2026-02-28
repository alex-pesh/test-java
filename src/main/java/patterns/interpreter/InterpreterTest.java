package patterns.interpreter;

/**
 * https://www.youtube.com/watch?v=vCB9wrRA5PM
 * https://coderlessons.com/articles/java/primer-shablona-interpretatora
 */
public class InterpreterTest {

	public static void main(String[] args) {
		String expr = "1 + 2 + 3 - 4 - 2 + 125 + 10";
		Expression eval = new Evaluator(expr);

		System.out.println(eval.interpret(eval));
	}
}
