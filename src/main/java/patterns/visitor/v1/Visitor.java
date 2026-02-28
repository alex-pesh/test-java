package patterns.visitor.v1;

import patterns.visitor.v1.objects.*;

public interface Visitor {

	void visit(Shape shape);
	void visit(Dot dot);
	void visit(Circle circle);
//	void visit(Rectangle rectangle);
	void visit(CompoundGraphic compound);

//	void visit(Graphic graphic);

}
