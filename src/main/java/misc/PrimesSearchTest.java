package misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimesSearchTest {

	public static class EratosthenesSieve {

		List<Integer> getPrimes(int max) {

			boolean[] sieve = new boolean[max];
			Arrays.fill(sieve, true);
			sieve[0] = false;
			sieve[1] = false;

			for (int i = 2,  i_sqrt; (i_sqrt = i * i) < max; i++) {
				if (sieve[i]) {
					for (int j = i_sqrt; j < max; j += i) {
						sieve[j] = false;
					}
				}
			}

			var primes = new ArrayList<Integer>();
			for (int i = 2; i < sieve.length; i++) {
				if (sieve[i]) {
					primes.add(i);
				}
			}

			return primes;
		}

	}


	public static void test() {
		var primes = new EratosthenesSieve().getPrimes(1_000);

		for (int prime : primes) {
			System.out.printf("%d, ", prime);
		}

		System.out.println();
		System.out.printf("Count: %d\n", primes.size());
		System.out.printf("Last value: %d\n", primes.get(primes.size() -1));
	}

}
