package algos.string;

import java.util.HashMap;
import java.util.Map;

/**
 * Алгоритм Бойера-Мура для поиска подстроки
 * https://www.youtube.com/watch?v=CKxBGZdN0t8&list
 */
public class BoyerMooreSearch {

	public static void main(String[] args) {

		int result = -1;

		String src =
				"""
				Stream operations are divided into intermediate and terminal operations
				""";

		String pattern = "are";

		long start = System.nanoTime();

		result = find(src, pattern);
//		result = src.indexOf(pattern);


		long end = System.nanoTime();
		System.out.println("Time: " + (end - start));

		if (result > -1) {
			System.out.println("Found at index " + result);
		} else {
			System.out.println("Not found");
		}
	}



	public static int find(String srcStr, String patternStr) {

		char[] src = srcStr.toCharArray();
		int srcLength = src.length;
		char[] pattern = patternStr.toCharArray();
		int patternLength = pattern.length;
		Map<Character, Integer> shiftsMap = new HashMap<>();

		for (int i = 0; i < patternLength; i++) {
			int shift = Math.max(1, patternLength - i - 1);
			shiftsMap.put(pattern[i], shift);
		}

		int shift = 0;
		for(;;) {
			int lastInSrcIdx = shift + patternLength - 1;
			if (lastInSrcIdx >= srcLength) {
				return -1;
			}

			char lastInSrc = src[shift + patternLength - 1];
			if (pattern[patternLength - 1] == lastInSrc) {
				for (int i = patternLength - 1; i >= 0; i--) {
					if (pattern[i] != src[shift + i]) {
						break;
					} else if (i == 0) {
						return shift;
					}
				}
			}

			Integer charShift = shiftsMap.get(lastInSrc);
			shift += charShift == null ? patternLength : charShift;
		}
	}

}
