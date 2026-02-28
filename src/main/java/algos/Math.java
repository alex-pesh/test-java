package algos;

public class Math {

	public static void main(String[] args) {

		circleCount(5, 5);

	}


	/**
	 * Пример использования оператора XOR при обмене значений переменных
	 * <p>
	 * <blockquote><pre>
	 *     (A XOR B) XOR B = A
	 *     (A XOR B) XOR A = B
	 * </pre></blockquote><p>
	 * <a href="https://javarush.com/groups/posts/1925-pobitovihe-operacii">Побитовые операции в Java</a>
	 */
	public static void xorSwap() {

		int a = 132;
		int b = 44;

		a = a ^ b;
		b = a ^ b;
		a = a ^ b;

		System.out.println("a = " + a);
		System.out.println("b = " + b);
	}


	public static int log2(int value) {
		return (int) java.lang.Math.ceil(java.lang.Math.log(value) / java.lang.Math.log(2));
	}


	/**
	 * Использование оператора {@code &} для организации циклического счетчика от 0 до 2<sup>maxPowerOfTwo</sup>-1 с игнорированием sign bit.
	 *
	 */
	public static void circleCount(int maxPowerOfTwo, int divider) {
		int MAX_VALUE = (int) (java.lang.Math.pow(2, maxPowerOfTwo)) - 1;

		for (int i = 1; i <= MAX_VALUE * 2; i++) {
			System.out.println((i & MAX_VALUE) % divider);
		}
	}

}
