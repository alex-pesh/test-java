package core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

public class StringOps {

	public static void parallelSortFromFile() {

		try {
			byte[] bytes = Files.readAllBytes(Path.of("/home/alexander/c++ road.txt"));
			String content = new String(bytes, StandardCharsets.UTF_8);
			String[] words = content.split("[\\P{L}]+");
			Arrays.parallelSort(words, Comparator.comparingInt(String::length));

			System.out.println(words);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void offsetByCodePoint() {
		String s_1 = "ABC\uD835\uDD6b_D"; // offset с учетом размерности символа (2 байта)
		int index_1 = s_1.offsetByCodePoints(0, 4);
		System.out.println("String: " + s_1);
		System.out.println("Index: " + index_1 + "; value at index: " + s_1.charAt(index_1) + System.lineSeparator());

		String s_2 = "ABCz_D";
		int index_2 = s_2.offsetByCodePoints(0, 4);
		System.out.println("String: " + s_2);
		System.out.println("Index: " + index_2 + "; value: at index: " + s_2.charAt(index_2));
	}

}
