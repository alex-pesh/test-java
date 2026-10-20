package algos.sorting;

/**
 * <h>Сортировка пузырьком</h>
 * Будем просматривать весь массив "снизу вверх" и менять стоящие рядом элементы в там случае, если "нижний"
 * элемент меньше, чем "верхний". Таким образом, мы вытолкнем наверх самый "легкий” элемент всего массива.
 * Теперь повторим для оставшихся неотсортироваными N-1 элементов (т.е. для тех, которые лежат "ниже" первого)
*/
public class BubbleSorting {

	public static void main(String[] args) {

		int length = 100_000;

/*
		int[] arr = new int[length];
		for (int i = 0; i < length; i++)  {
			arr[i] = (int) (Math.random() * 1000);
		}
*/

		int[] arr = new int[] {1,2,5,3};

		long start = System.nanoTime();
		sort_2(arr);
		long end = System.nanoTime();

		for (int i = 0; i < arr.length; i++) {
			System.out.printf("%d,", arr[i]);
		}

		System.out.println();
		System.out.println("Time taken (us): " + (end - start)/1000);

	}


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
