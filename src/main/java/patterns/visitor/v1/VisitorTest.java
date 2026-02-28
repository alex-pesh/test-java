package patterns.visitor.v1;

import patterns.visitor.v1.objects.*;

public class VisitorTest {

	public static void main(String[] args) {

		Visitor visitor = new ExportVisitor();

		Graphic graphic = new CompoundGraphic(new Shape[] {
				new Shape("Custom shape"),
				new Dot(),
				new Rectangle(),
				new Circle()
		});

		graphic.accept(visitor);


//		graphic = new Dot();
//		graphic.accept(visitor);
	}

}
