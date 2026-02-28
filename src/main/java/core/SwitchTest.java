package core;

/**
 *  See also {@link core.sealed.Payment}
 */
public class SwitchTest {

	public static void main(String[] args) {
//		typeSwitchTest("0.3");
		unpackRecordTest();
	}


	public static void typeSwitchTest(Object o) {
		double value = switch (o) {
			case Integer i -> i.doubleValue();
			case Float f -> f.doubleValue();
			case String s -> Double.parseDouble(s);
			default -> 0d;
		};

		System.out.println(value);
	}


	static double getDoubleValueUsingGuardedPatterns(Object o) {
		return switch (o) {
			case String s when s.length() > 0 && !(s.contains("#") || s.contains("@")) -> Double.parseDouble(s);
			case null -> 0d;
			default -> 0d;
		};
	}



	public static void unpackRecordTest() {
		printArea(new Rectangle(new Point(10, 80), new Point(50, 60)));
	}


	record Point(int x, int y) {}
	record Rectangle(Point topLeft, Point bottomRight) {}

	static void printArea(Object obj) {
		if (obj instanceof Rectangle (
				Point(int x1, int y1), Point(int x2, int y2) )) {

			int width = x2 - x1;
			int height = y2 - y1;
			System.out.println("Area: " + Math.abs(width * height));
		}
	}


	// ---------- Generics pattern match --------

	record Box<T>(T content) {}

	public static void switchGenericRecord() {
		Box<?> bbs = new Box<>(null);

		switch (bbs) {
			case Box(Box(String s))  -> System.out.println("Box(Box(String s))");
			case Box(Box b)  -> System.out.println("Box(Box b)");
			case Box b  -> System.out.println("Box b");
			case null -> System.out.println("Null");
		}
	}


	public static void switchUnboxedGenericRecord() {
		Box<Box<Object>> bbs = new Box<>(new Box<>("boxed string"));
		Box<Box<Long>> bbl = new Box<>(new Box<>(1L));
		Box<Box<Object>> bbo = new Box<>(new Box<>(new Object()));
		Box<?> bbe = new Box<>(null);

		Box<?> box = bbe;

		Object unboxed = switch (box) {
			case Box(Box(String s))  -> s;
			case Box(Box(Object o))  -> o;
			case Box(var b) when b == null -> "empty box";
			default -> null;
		};

		System.out.println("Unboxed value: " + unboxed);
	}


}
