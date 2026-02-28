package algos.sorting;

public class QuickSort {

	public static void main(String[] args) {

		System.out.println("Quick Sorting");

		int length = 1_000_000;
		int[] arr = new int[length];

/*		for (int i = 0; i < length; i++)  {
			arr[i] =  length - i;
		}*/

		for (int i = 0; i < length; i++)  {
			arr[i] = (int) (Math.random() * 1000000);
		}

//		int[] arr = new int[] {2, 3, 5, 2, 3, 1, 0, 56, -3, 4};

//		printArray("Initial array: ", arr);

		long start = System.nanoTime();
		sort(arr);
		long end = System.nanoTime();

//		printArray("Sorted array: ", arr);

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
	 * https://www.youtube.com/watch?v=aY0yYfztKMY
	 */
	private static void qSort(int[] arr, int low, int high) {
		if (low >= high) {
			return;
		}

		int partitionIdx = partition(arr, low, high);
		qSort(arr, low, partitionIdx - 1);
		qSort(arr, partitionIdx + 1, high);
	}

	// swapIdx будет увеличиваться до тех пор, пока он станет указывать на первый элемент, больший pivot.
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

		swap(arr, swapIdx, high);

		return swapIdx;
	}

	private static void swap(int[] arr, int fromIdx, int toIdx) {
		int tmp = arr[fromIdx];
		arr[fromIdx] = arr[toIdx];
		arr[toIdx] = tmp;
	}

}








