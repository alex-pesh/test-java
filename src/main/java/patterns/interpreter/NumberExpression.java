package patterns.interpreter;

public class NumberExpression implements Expression {

	private final int value;

	public NumberExpression(int value) {
		this.value = value;
	}

	@Override
	public int interpret(Expression ctx) {
		return value;
	}
}
