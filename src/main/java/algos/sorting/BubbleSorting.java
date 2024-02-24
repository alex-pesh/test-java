package algos.sorting;

public class BubbleSorting {

	public static int sort_1(int[] array) {
		boolean sorted = false;
		int counter = 0;
		while (!sorted) {
			sorted = true;
			for (int i = 1; i < array.length; i++) {
				counter++;
				int prev = array[i-1];
				int current = array[i];
				if (prev > current) {
					swap(array, i - 1, i);
					sorted = false;
				}
			}
		}

		return counter;
	}


	public static int sort_2(int[] array) {
		int counter = 0;
		for (int i = 0; i < array.length -1; i++) {
			for (int j = 0; j < array.length - i - 1; j++) {
				counter++;
				if (array[j] > array[j + 1]) {
					swap(array, j, j + 1);
				}
			}
		}

		return counter;
	}

	private static void swap(int[] array, int idx1, int idx2) {
		int tmp = array[idx1];
		array[idx1] = array[idx2];
		array[idx2] = tmp;
	}


}
