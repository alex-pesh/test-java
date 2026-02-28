package algos.sorting;

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
			int minIdx = i;
			for (int j = i + 1; j < length; j++) {
				if (arr[j] < arr[minIdx]) {
					minIdx = j;
				}
			}

			int tmp = arr[i];
			arr[i] = arr[minIdx];
			arr[minIdx] = tmp;
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
