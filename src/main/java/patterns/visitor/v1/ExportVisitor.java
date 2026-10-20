package patterns.visitor.v1;

import patterns.visitor.v1.objects.*;

import java.util.HashMap;
import java.util.Map;

public class ExportVisitor implements Visitor {


	@Override
	public void visit(Shape shape) {
		System.out.println("Exporting shape: 	 " + shape);
	}

	@Override
	public void visit(Dot dot) {
		System.out.println("Exporting dot:		 " + dot);
	}

	@Override
	public void visit(Circle circle) {
		System.out.println("Exporting circle:	 " + circle);
	}

//	@Override
//	public void visit(Rectangle rectangle) {
//		System.out.println("Exporting rectangle: " + rectangle);
//	}

	@Override
	public void visit(CompoundGraphic compound) {
		System.out.println("Exporting compound	");
	}



/*
	// Минусы подхода с переключением (switch):
	// - Недостаточная масштабируемость: Добавление новых узлов потребует обновления тела каждого метода-визитора,
	//	 увеличивая сложность поддержки и повышения риска ошибок.
	// - Ограниченная выразительность: Оператор switch хорош лишь тогда, когда количество вариантов ограничено.
	//	 Чем больше возможных типов узлов, тем сложнее поддерживать такую структуру.
	// - Невозможность повторного использования логики: Логика визита одного узла повторно использовать невозможно
	// 	 без копирования её между разными методами визитов.
	@Override
	public void visit(Graphic graphic) {
		switch (graphic) {
			case Rectangle obj -> System.out.println("Exporting rectangle: " + obj);
			case Circle obj -> System.out.println("Exporting circle: " + obj);
			case Dot obj -> System.out.println("Exporting dot: " + obj);
			case Shape obj -> System.out.println("Exporting shape: " + obj);
			case CompoundGraphic obj -> System.out.println("Exporting compound: " + obj);

			default -> throw new UnsupportedOperationException("Unknown Graphic type: " + graphic);
		}
	}
*/

}
