package algos.sorting;

/**
 * <h4>Сортировка выбором</h4>
 * При просмотре массива будем искать наименьший элемент, сравнивая его с первым. Если такой элемент найден,
 * поменяем его местами с первым. Затем повторим эту операцию, но начнем не с первого элемента, а со второго.
 * И будем продолжать подобным образом, пока не рассортируем весь массив. Таким образом наименьший элемент двигается в
 * начало массива.
 * <p>
 * https://rutube.ru/video/6e760a6127816b63dd88ee093fea2c39/?t=244&r=plwd
 * <br>
 * По сравнению с сортировкой вставками, у сортировки выбором меньшее количество перестановок, но большее количество сравнений
 */
public class SelectionSort {

	public static void main(String[] args) {

//		int length = 1000;
//		int[] arr = new int[length];
//		for (int i = 0; i < length; i++) {
//			arr[i] = length - i;
//		}

		int[] arr = new int[] {2, 5, 0, 1, 2, 2, 0, 3, 8};


		printArray("Initial array: ", arr);

		long start = System.nanoTime();
		sort(arr);
		long end = System.nanoTime();

		printArray("Sorted array: ", arr);

		System.out.println("Time taken (us): " + (end - start)/1000);
	}


	public static void sort(int[] arr) {

		int length = arr.length;
		for (int i = 0; i < length; i++) {
			int minIdx = i; // нарекаем элемент массива самым наименьшим и запоминаем его индекс
			for (int j = i + 1; j < length; j++) {
				if (arr[j] < arr[minIdx]) { // затем во вложенном цикле сравниваем все оставшиеся с этим наименьшим
					minIdx = j; // если какой-то элемент оказывается меньше запомненного минимального, то нарекаем его минимальным
				}
			}
			// В итоге minIdx будет содержать самый минимальный элемент. И если он изменился, это значит, что элемент
			// по этому индексу стоит не на своем месте и его нужно поменять местами с текущим i-тым
			if (minIdx != i) {
				int tmp = arr[i];
				arr[i] = arr[minIdx];
				arr[minIdx] = tmp;
			}
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
