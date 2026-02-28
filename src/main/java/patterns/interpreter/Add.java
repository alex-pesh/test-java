package patterns.interpreter;

public class Add implements Expression {

	private final Expression left;
	private final Expression right;

	public Add(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public int interpret(Expression ctx) {
		return left.interpret(ctx) + right.interpret(ctx);
	}

}
