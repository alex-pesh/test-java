package core;

import java.lang.reflect.Array;

public class ReflectionTest {

	public static void main(String[] args) {
		arrayReflectionTest();
	}



	public static void arrayReflectionTest() {
		System.out.println( areArrayElementsEqual(new int[] {1, 2, 3}, new long[] {1, 2, 3}) );
		System.out.println( areArrayElementsEqual(new int[] {1, 2, 3}, new Integer[] {1, 2, 3}) );
	}

	private static boolean areArrayElementsEqual(Object actualArray, Object expectedArray) {
		for (int i = 0; i < Array.getLength(actualArray); i++) {
			if (!Array.get(actualArray, i).equals(Array.get(expectedArray, i))) {
				return false;
			}
		}

		return true;
	}

}
