package algos.misc;

/**
 * https://youtu.be/cyIw3NKfdGw?list=PLIJLLSrXDPogL-Db1f9IvHp2fiMwX18yz&t=1233
 */
public class Fibonacci {

	public static void main(String[] args) {
		System.out.println(fibTabulation(1000));
	}


	public static long fibRecursion(int n) {
		if (n == 0) return 0;
		if (n == 1) return 1;
		long a = fibRecursion(n-2);
		long b = fibRecursion(n-1);

		return a + b;
	}


	public static long fibRecursionCached(int n) {
		long[] cache = new long[n + 1];
		return fibRecursion(n, cache);
	}

	private static long fibRecursion(int n, long[] cache) {
		if (n == 0) return 0;
		if (n == 1) return 1;

		if (cache[n] == 0) {
			long a = fibRecursion(n-2, cache);
			long b = fibRecursion(n-1, cache);
			cache[n] = a + b;
		}

		return cache[n];
	}



	public static long fibTabulation(int n) {
		if (n == 0) return 0;
		if (n == 1) return 1;

		long[] cache = new long[n + 1];
		cache[0] = 0;
		cache[1] = 1;

		for (int i = 2; i <=n; i++) {
			cache[i] = cache[i-1] + cache[i-2];
		}

		return cache[n];
	}

}
