package patterns.visitor.v1.objects;

import patterns.visitor.v1.Visitor;

public class Shape implements Graphic {
	protected String name;

	public Shape(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public void draw() {

	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
