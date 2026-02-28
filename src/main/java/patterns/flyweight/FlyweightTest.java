package patterns.flyweight;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://www.youtube.com/watch?v=SsCtAmlatsI
 */
public class FlyweightTest {

	public static void main(String[] args) {

		FlyweightFactory.getSymbol("j", "Arial");
		FlyweightFactory.getSymbol("a", "Arial");
		FlyweightFactory.getSymbol("v", "Arial");
		FlyweightFactory.getSymbol("a", "Arial");


		AtomicInteger counter = new AtomicInteger();
		for (Character c : "java".toCharArray()) {
			PositionContext position = new PositionContext(1, counter.addAndGet(1));
			Drawable drawable = FlyweightFactory.getSymbol(c.toString(), "Arial");
			drawable.draw(position);
		}
	}

}
