package patterns.visitor.v1.objects;

import patterns.visitor.v1.Visitor;

public class Rectangle extends Shape {
	protected int width, height;

	public Rectangle() {
		super("Rectangle");
	}

	@Override
	public void draw() {
		System.out.println("Draw " + Rectangle.class.getSimpleName());
	}

/*		@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}*/

}
