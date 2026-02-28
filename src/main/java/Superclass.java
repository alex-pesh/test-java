public class Superclass {

	public static final String VALUE = "=== VALUE ===";

	private int value;

	{
		System.out.println("1 - Value: " + value);
	}

	static {
		System.out.println("2 - Value: " + Superclass.VALUE);
	}

	public Superclass() {
		System.out.println("3 - Constructor");
		value = 999;
	}


	public static void main(String... args) {
//		System.out.println("4 - Value: ");
//		System.out.println("4 - Value: " + Superclass.VALUE);
//		System.out.println("4 - Value: " + new Superclass().value);

		String s = "   ddd";

		System.out.println(s.trim());

	}

}

