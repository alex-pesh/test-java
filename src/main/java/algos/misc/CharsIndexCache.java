package algos.misc;

public class CharsIndexCache {

	public static void main(String[] args) {
		char[] chars = new char[] {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
		int size = tableSizeFor((int) (chars.length + (chars.length*0.75))) - 1;

/*		for (char value : chars) {

			System.out.printf("%d - %d%n", (int) value, (7 & value));

//			System.out.printf("%d - %d%n", (int) value, charIndex(value, size));
		}*/

		int total = tableSizeFor(chars.length) - 1 ;
		for (char c : chars) {
			System.out.printf("%d - %d%n", (int) c, (total & c));
		}
	}

	static int charIndex(char value, int total) {
		return total & (value ^ (value >>> 16));
	}

	/**
	 * Returns a power of two size for the given target capacity.
	 */
	static int tableSizeFor(int cap) {
		int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
		return (n < 0) ? 1 : n + 1;
	}


}
