package patterns.flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {

	private static Map<String, Drawable> drawableMap = new HashMap<>();

	public static Drawable getSymbol(String name, String style) {
		return drawableMap.computeIfAbsent(String.join(name, "_", style), _ -> new Symbol(name, style));
	}

}
