package patterns.interpreter;

public interface Expression {

	int interpret(Expression ctx);

}
