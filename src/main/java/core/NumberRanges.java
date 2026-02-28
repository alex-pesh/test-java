package core;

public class NumberRanges {

	public static void main(String[] args) {
		testInteger();
	}


	public static void testRanges() {
		System.out.println("Long range");
		System.out.println(Float.MIN_VALUE);
		System.out.println(Float.MAX_VALUE);

		System.out.println("\nDouble range");
		System.out.println(Double.MIN_VALUE);
		System.out.println(Double.MAX_VALUE);
	}


	/**
	 * https://www.thegeekyway.com/java-autoboxing-xxautoboxcachemax/
	 *
	 * <p>Integer cache is by default (-128 to 127), but can be increased by setting VM arg <code>-XX:AutoBoxCacheMax=SIZE</code>.
	 * This changing size only works for positive integer and it can't be set less than 127
	 */
	@SuppressWarnings("removal")
	public static void testInteger() {
		int int1 = 100;
		int int2 = 100;
		System.out.println("int1 == int2: 				" + (int1 == int2));    // true as they are primitive

		Integer integerObj1 = new Integer(100);
		Integer integerObj2 = new Integer(100);
		System.out.println("integerObj1 == integerObj2: " + (integerObj1 == integerObj2));   // always false as these are different objects

		Integer integer1 = -128;
		Integer integer2 = -128;
		System.out.println("integer1 == integer2: 		" + (integer1 == integer2));   // true

		Character c1 = 110;
		Character c2 = 110;
		System.out.println("c1 == c2 : 					" + (c1 == c2));    //true

		Short s1 = 127;
		Short s2 = 127;
		System.out.println("s1 == s2: 					" + (s1 == s2));    //true
	}


	public static void testFloat() {
		// An example of overflow:
		double d = 1e308;
		System.out.print("overflow produces infinity: ");
		System.out.println(d + "*10==" + d * 10);

		// An example of gradual underflow:
		d = 1e-305 * Math.PI;
		System.out.print("gradual underflow: " + d + "\n   ");
		for (int i = 0; i < 4; i++)
			System.out.print(" " + (d /= 100000));
		System.out.println();

		// An example of NaN:
		System.out.print("0.0/0.0 is Not-a-Number: ");
		d = 0.0 / 0.0;
		System.out.println(d);

		// An example of inexact results and rounding:
		System.out.print("inexact results with float:");
		for (int i = 0; i < 100; i++) {
			float z = 1.0f / i;
			if (z * i != 1.0f)
				System.out.print(" " + i);
		}
		System.out.println();

		// Another example of inexact results and rounding:
		System.out.print("inexact results with double:");
		for (int i = 0; i < 100; i++) {
			double z = 1.0 / i;
			if (z * i != 1.0)
				System.out.print(" " + i);
		}
		System.out.println();

		// An example of cast to integer rounding:
		System.out.print("cast to int rounds toward 0: ");
		d = 12345.6;
		System.out.println((int) d + " " + (int) (-d));
	}

}
