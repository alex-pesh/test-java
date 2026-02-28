package misc;

/**
 * https://tproger.ru/articles/awesome-bits
 */
public class BitOps {

	public static void main(String[] args) {

		int i = 11;
		int ii = (i << 1) | 1; // 2i + 1
		System.out.printf("2 * %d + 1 = %d\n", i, ii);

		ii = (i >> 1); // i/2
		System.out.printf("%d / 2 = %d\n", i, ii);

	}

}
