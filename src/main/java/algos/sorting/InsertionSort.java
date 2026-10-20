package algos.sorting;

/**
 * <h4>Сортировка вставками.</h4>
 *
 * https://www.youtube.com/watch?v=jywoZ2XaQoM
 * <br>
 * https://rutube.ru/video/6e760a6127816b63dd88ee093fea2c39/?t=312&r=plwd
 * <br>
 * По сравнению с сортировкой выбором, у сортировки вставками меньшее количество сравнений, но большее количество перестановок
 *
 */
public class InsertionSort {

	public static void main(String[] args) {

/*
		int length = 100_000;
		int[] arr = new int[length];
		for (int i = 0; i < length; i++)  {
			arr[i] = (int) (Math.random() * 1000);
		}
*/

		int[] arr = new int[] {2, 3, 5, 2, 0, 3, 1};

		printArray("Initial array: ", arr);

		long start = System.nanoTime();
		sort_2(arr);
		long end = System.nanoTime();

		printArray("Sorted array: ", arr);

		System.out.println("Time taken (us): " + (end - start)/1000);
	}


	private static void sort(int[] arr) {

		int length = arr.length;
		for (int i, j = 0; ++j < length;) {
			int ai = arr[i = j];

			if (ai < arr[i - 1]) {
				while (--i >= 0 && ai < arr[i]) {
					arr[i + 1] = arr[i];
				}
				arr[i + 1] = ai;
			}
		}
	}


	public static void sort_2(int[] arr) {

		int length = arr.length;
		for (int i = 0; i < length; i++) {
			int ai = arr[i];
			int j = i;
			while (j > 0 && arr[j - 1] > ai) {
				arr[j] = arr[j - 1];
				j--; // каждый раз во внутреннем цикле while возвращаемся назад и сравниваем все предыдущие элементы с текущим i-тым
			}
			arr[j] = ai;
		}
	}


	static void printArray(String label, int[] arr) {
		System.out.println(label);
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println();
	}

}
