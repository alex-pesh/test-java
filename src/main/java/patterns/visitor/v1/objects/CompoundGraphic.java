package patterns.visitor.v1.objects;

import patterns.visitor.v1.Visitor;

public class CompoundGraphic implements Graphic {
	private Graphic[] children;

	public CompoundGraphic(Shape[] children) {
		this.children = children;
	}

	@Override
	public void draw() {
		System.out.println("Draw " + CompoundGraphic.class.getSimpleName());
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		for (Graphic graphic : children) {
			graphic.accept(visitor);
		}
	}

}
