package algos.sorting;

import java.util.Arrays;

public class MergeSort {

	public static void main(String[] args) {

		System.out.println("Merge Sorting");

		int length = 1_000_000;
		int[] arr = new int[length];
		for (int i = 0; i < length; i++)  {
			arr[i] = (int) (Math.random() * 1000000);
		}

//		int[] arr = new int[] {2, 3, 5, 2, 3, 1, 0, 56, -3, 4};
//
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
		mergeSort(arr, 0, arr.length - 1);
	}


	/**
	 * Рекурсивно массив разбивается на две части до тех пор, пока не останется два элемента.
	 * <br>
	 * Затем берутся по два внутренних массива, попарно сравниваются их элементы и в порядке возрастания
	 * сливаются в исходный массив в то место, откуда был взят левый подмассив.
	 *
	 */
	private static void mergeSort(int[] arr, int low, int high) {
		if (high <= low) {
			return;
		}

		int middle =  low + (high - low)/2;
		mergeSort(arr, low, middle);
		mergeSort(arr, middle + 1, high);

		merge(arr, low, middle, high);
	}

	private static void merge(int[] arr, int low, int middle, int high) {

		int leftLen = middle - low + 1;
		int rightLen = high - middle;

		int[] left = new int[leftLen];
		int[] right = new int[rightLen];

		System.arraycopy(arr, low, left, 0, leftLen);
		System.arraycopy(arr, middle + 1, right, 0, rightLen);

		int i = 0, j = 0, k = low;

		while (i < leftLen && j < rightLen) {
			if (left[i] <= right[j]) {
				arr[k] = left[i];
				i++;
			} else {
				arr[k] = right[j];
				j++;
			}
			k++;
		}

		while (i < leftLen) {
			arr[k++] = left[i++];
		}

		while (j < rightLen) {
			arr[k++] = right[j++];
		}
	}


	private static int[] merge2(int[] a1, int[] a2) {

		int i = 0, j = 0, k = 0;
		int a3[] = new int[a1.length + a2.length];

		while (i < a1.length && j < a2.length) {
			a3[k++] = a1[i] < a2[j] ? a1[i++] : a2[j++];
		}

		return a3;
	}

}
