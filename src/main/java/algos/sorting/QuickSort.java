package algos.sorting;

public class QuickSort {

	public static void main(String[] args) {

		System.out.println("Quick Sorting");

/*
		int length = 1_000_000;
		int[] arr = new int[length];

		for (int i = 0; i < length; i++)  {
			arr[i] = (int) (Math.random() * 1000000);
		}
*/

		int[] arr = new int[] {3, 2, 5, 2, 6, 4};

		printArray("Initial array: ", arr);

		long start = System.nanoTime();
		sort(arr);
		long end = System.nanoTime();

		printArray("Sorted array: ", arr);

		System.out.println("Time taken (us): " + (end - start)/1000);
	}


	static void printArray(String label, int[] arr) {
		System.out.println(label);
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println();
	}


	private static void sort(int[] arr) {
		qSort(arr, 0, arr.length - 1);
	}


	/**
	 * https://www.techiedelight.com/ru/quicksort/
	 * <br>
	 * https://www.youtube.com/watch?v=aY0yYfztKMY
	 * <br>
	 * https://rutube.ru/video/7451a25dec372174e78eb2cdaa10d897/
	 * <br>
	 * https://rutube.ru/video/a886aeaef8c5bd65cd0bdad03a6ceff7/
	 * <br>
	 * https://rutube.ru/video/6e760a6127816b63dd88ee093fea2c39/?t=312&r=plwd
	 * <p>
	 * 1. Выбор опорного элемента (pivot). Из массива выбирается элемент, который будет служить точкой разделения.
	 * Это может быть первый, последний, средний или случайный элемент. От выбора pivot сильно зависит производительность алгоритма.
	 * <br>
	 * 2. Разделение (разбиение) массива. Все элементы массива перераспределяются так, чтобы все элементы, меньшие опорного,
	 * оказались слева от него, а большие или равные — справа. В результате опорный элемент оказывается на своём
	 * окончательном месте в отсортированном массиве.
	 * <br>
	 * 3. Рекурсивная сортировка подмассивов. Алгоритм рекурсивно применяется к левой и правой частям массива (если в них больше одного элемента).
	 */
	private static void qSort(int[] arr, int low, int high) {
		if (low >= high) {
			return;
		}

		int partitionIdx = hoarePartition(arr, low, high);
		qSort(arr, low, partitionIdx);
		qSort(arr, partitionIdx + 1, high);
	}


	// Схема Ломуто.
	// swapIdx будет увеличиваться до тех пор, пока он станет указывать на первый элемент, больший pivot.
	// Возвращаемое значение будет индексом опорного элемента (pivot) которая будет разделителем для следующих рекурсивных вызовов.
	// Далее по этому индексу будет происходить разбиение массива на две части для их сортировки.
	private static int partition(int[] arr, int low, int high) {

		int pivot = arr[high];
		int swapIdx = low;

		for (int i = low; i < high; i++) {
			if (arr[i] <= pivot) {
				if (i != swapIdx) {
					swap(arr, i, swapIdx);
				}
				swapIdx++;
			}
		}

		// Когда цикл завершен, все меньшие элементы стоят слева от swapIdx, а большие — справа
		// (на индексах от swapIdx до high - 1). Теперь на позицию swapIdx ставится сам опорный элемент.
		// После этого обмена он оказывается точно на своем финальном месте в полностью отсортированном массиве
		// на позицию swapIdx ставится сам опорный элемент
		swap(arr, swapIdx, high);

		return swapIdx;
	}


	// Схема Хоара
	// - Опорный элемент обычно выбирается как середина или левый край.
	// - Указатель i начинает движение слева направо. Он ищет первый элемент, который больше или равен опорному.
	// - Указатель j начинает движение справа налево. Он ищет первый элемент, который меньше или равен опорному.
	// - Когда оба таких элемента найдены, они меняются местами. После этого i увеличивается, а j уменьшается.
	// - Процесс продолжается, пока индексы i и j не пересекутся (i > j).
	// - Функция возвращает индекс j. Важно понимать: этот индекс не указывает на финальное место опорного элемента.
	//   Он лишь разделяет массив на две части, которые затем сортируются рекурсивно.
	//
	// При использовании левая часть должна быть 		qSort(arr, low, partitionIdx);
	private static int hoarePartition(int[] arr, int low, int high) {
		int pivot = arr[low + (high - low) / 2]; // Выбор середины
		int i = low - 1;
		int j = high + 1;

		while (true) {
			do {
				i++;
			} while (arr[i] < pivot); // Ищем >= pivot слева

			do {
				j--;
			} while (arr[j] > pivot); // Ищем <= pivot справа

			if (i >= j) {
				return j; // Точка пересечения
			}
			swap(arr, i, j); // Меняем ошибочные элементы
		}
	}

	private static void swap(int[] arr, int fromIdx, int toIdx) {
		int tmp = arr[fromIdx];
		arr[fromIdx] = arr[toIdx];
		arr[toIdx] = tmp;
	}

}








