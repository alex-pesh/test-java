package algos.misc;

public class SlidingWindow {

	public static void main(String[] args) {
		int[] array = new int[] {2, 11, 5, 7, 10, 1, 0, 6, 3};
		System.out.println(maxSum(array, 3));
	}


	/**
	 * Найти максимальную сумму подмассива длины k в массиве чисел.
	 * Вместо вложенного цикла с O(n*k), используется скользящее окно с O(n).
	 * Чтобы повторно использовать рассчитанную для предыдущего подмассива сумму, достаточно вычесть элемент
	 * выпадающий из окна и добавить новый, попавший в окно.
	 */
	private static int maxSum(int[] array, int k) {
		int maxSum = 0;
		int winSum = 0;

		for (int i = 0; i < k; i++) {
			winSum += array[i];
		}

		maxSum = winSum;

		for (int i = k; i < array.length; i++) {
			winSum += array[i] - array[i - k];
			maxSum = Math.max(maxSum, winSum);
		}

		return maxSum;
	}

}
