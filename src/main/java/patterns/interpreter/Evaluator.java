package patterns.interpreter;

import java.util.LinkedList;

public class Evaluator implements Expression {

	private final String expression;

	public Evaluator(String expression) {
		this.expression = expression.replaceAll("\\s", "");
	}

	@Override
	public int interpret(Expression ctx) {

		LinkedList<Expression> stack = new LinkedList<>();

		String[] numbers = expression.split("\\D");
		for (int i = numbers.length; i > 0; i--) {
			int number = Integer.parseInt(numbers[i - 1]);
			stack.push(new NumberExpression(number));
		}


		String[] operations = expression.split("\\d");
		for (String operation : operations) {
			Expression left, right;
			switch (operation) {
				case "+":
					left = stack.pop();
					right = stack.pop();
					stack.push(new Add(left, right));
					break;
				case "-":
					left = stack.pop();
					right = stack.pop();
					stack.push(new Substract(left, right));
					break;
			}
		}

		Expression eval = stack.pop();

		return eval.interpret(ctx);
	}

}
