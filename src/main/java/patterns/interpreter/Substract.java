package patterns.interpreter;

public class Substract implements Expression {

	private final Expression left;
	private final Expression right;

	public Substract(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public int interpret(Expression ctx) {
		return left.interpret(ctx) - right.interpret(ctx);
	}

}
