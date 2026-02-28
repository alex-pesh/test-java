package patterns.visitor.v1.objects;

import patterns.visitor.v1.Visitor;

public interface Graphic {

	void draw();

	default void accept(Visitor visitor) {
//		throw new UnsupportedOperationException("Method is not implemented by class: " + getClass());
	};

}
