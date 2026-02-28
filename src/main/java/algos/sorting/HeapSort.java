package algos.sorting;

/**
 * https://habr.com/ru/companies/otus/articles/460087/
 */
public class HeapSort {

	public static void main(String[] args) {

		System.out.println("Heap Sorting");

/*
		int length = 1000_000;
		int[] arr = new int[length];
		for (int i = 0; i < length; i++) {
			arr[i] = (int) (Math.random() * 1000000);
		}
*/

		int[] arr = new int[]{2, 0, 5, 7, 2, 6, 3, 8, -1000};


		System.out.println("Initial array is: ");
		printArray(arr);

		HeapSort hs = new HeapSort();
		long start = System.nanoTime();
		hs.sort(arr);
		long end = System.nanoTime();

		System.out.println("Time taken (us): " + (end - start)/1000);

		System.out.println("Sorted array is: ");
		printArray(arr);
	}



	static void printArray(int[] arr) {
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println();
	}



	public void sort(int[] arr) {
		int n = arr.length;

		// Построение кучи (перегруппируем массив)
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(arr, n, i);
		}

		// Один за другим извлекаем элементы из кучи
		for (int i = n - 1; i >= 0; i--) {
			// Перемещаем текущий корень в конец
			int temp = arr[0];
			arr[0] = arr[i];
			arr[i] = temp;

			// Вызываем процедуру heapify на уменьшенной куче
			heapify(arr, i, 0);
		}
	}


	// Процедура для преобразования в двоичную кучу поддерева с корневым узлом i, что является
	// индексом в arr[]. n - размер кучи
	void heapify(int[] arr, int n, int i) {

		int largest = i; // Инициализируем наибольший элемент как корень
		int l = 2 * i + 1; // левый = 2*i + 1
		int r = 2 * i + 2; // правый = 2*i + 2

		// Если левый дочерний элемент больше корня
		if (l < n && arr[l] > arr[largest]) {
			largest = l;
		}

		// Если правый дочерний элемент больше, чем самый большой элемент на данный момент
		if (r < n && arr[r] > arr[largest]) {
			largest = r;
		}
		// Если самый большой элемент не корень
		if (largest != i) {
			int temp = arr[i];
			arr[i] = arr[largest];
			arr[largest] = temp;

			// Рекурсивно преобразуем в двоичную кучу затронутое поддерево
			heapify(arr, n, largest);
		}
	}
}
