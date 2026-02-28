package core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class StringOps {

	public static void main(String[] args) {
		format();
	}


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


	public static void printCodePoints() {
		String s = "Hello world! 👋";

		IntStream codePoints = s.codePoints();

		codePoints.forEach(cp -> System.out.println("Code Point: " + cp));

		// Process the stream converting it to hex
		System.out.println("\nCode Points in Hex:");
		s.codePoints().forEach(cp -> System.out.printf("U+%04x%n", cp));
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


	// https://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax
	public static void format() {

		String s = "qqq";
		System.out.printf("%15s%n", s); // pads left
		System.out.printf("%-15s%n", s); // pads right

	}

}
