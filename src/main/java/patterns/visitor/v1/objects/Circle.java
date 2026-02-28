package patterns.visitor.v1.objects;

import patterns.visitor.v1.Visitor;

public class Circle extends Dot {
	protected int radius;

	public Circle() {
		super("Circle");
	}

	@Override
	public void draw() {
		System.out.println("Draw " + Circle.class.getSimpleName());
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
