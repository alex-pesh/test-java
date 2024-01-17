package misc;

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

}
