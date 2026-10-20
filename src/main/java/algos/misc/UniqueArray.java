package algos.misc;

import java.util.Arrays;

/**
 * <h4>Удаление дубликатов.</h5>
 * Дан упорядоченный массив целых 32-разрядных чисел. Требуется удалить из него все повторения.
 */
public class UniqueArray {


	public static int[] create(int[] data) {

		// Указатель uniqueIdx будет сдвигаться вправо каждый раз, когда при передвижении указателя i
		// будет найден уникальный элемент, неравный текущему

		int uniqueIdx = 0;

		for (int i = 1; i < data.length; i++) {
			if (data[i] != data[uniqueIdx]) {
				uniqueIdx++;
				data[uniqueIdx] = data[i];
			}
		}

		data = Arrays.copyOf(data, uniqueIdx + 1);

		return data;
	}


	public static void main(String[] args) {
		int [] data = new int[] {34, 34, 34, 33, 30, 27, 27, 27, 23, 20};

		data = UniqueArray.create(data);

		for (int num : data) {
			System.out.print(num + " ");
		}
	}
}
