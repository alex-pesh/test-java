package algos.misc;

public class Factorial {

	public static void main(String[] args) {
		System.out.println(factorial(10));
	}


	public static long factorial(long n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}

		long result = 1;
		for (long l = 2; l <= n; l++) {
			result *= l;
		}

		return result;
	}

}
