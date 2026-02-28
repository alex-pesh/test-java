package patterns.visitor.v1.objects;

import patterns.visitor.v1.Visitor;

public class Dot extends Shape {
	protected int x, y;

	protected Dot(String name) {
		super(name);
	}

	public Dot() {
		super("Dot");
	}

	@Override
	public void draw() {
		System.out.println("Draw " + Dot.class.getSimpleName());
	}


	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
