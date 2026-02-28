package algos.string;

import java.nio.charset.Charset;

public class StringOps {

	public static void main(String[] args) {
		System.out.println(reverseStringXor("abcde"));
	}


	/**
	 * Пример использования оператора XOR при разворачивании строки
	 *
	 * @param str
	 * @return
	 */
	public static String reverseStringXor(String str) {

		char[] arr = str.toCharArray();
		int low = 0;
		int high = arr.length - 1;

		while (low < high) {
			arr[low] = (char) (arr[low] ^ arr[high]);
			arr[high] = (char) (arr[low] ^ arr[high]);
			arr[low] = (char) (arr[low] ^ arr[high]);
			low++;
			high--;
		}

		return new String(arr);
	}

}
