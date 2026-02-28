package core;

import lombok.SneakyThrows;

import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamTest {

	public static void main(String[] args) throws Exception {
		spliteratorTrySplitTest();
	}



//	выделить уникальные строки на основе их длины
	public static void stringsDistinctLength() {
		var result = Stream.of("foo", "bar", "baz", "quux")
				.map(DistinctByLength::new)
				.distinct()
				.map(DistinctByLength::str)
				.toList();

		System.out.println(result);
	}

	record DistinctByLength(String str) {

		@Override public boolean equals(Object obj) {
			return obj instanceof DistinctByLength(String str1) && str.length() == str1.length();
		}
		@Override public int hashCode() {
			return str.length();
		}
	}


	/**
	 * Используется дефолтный ForkJoinPool.commonPool() с параллелизмом по количеству ядер
	 */
	public static void parallelTest() {

		List<Integer> list = IntStream.range(1, 17).boxed().toList();
		list.parallelStream()
				.forEach(i -> {
					System.out.println("Start thread  " + Thread.currentThread().threadId() + ": " + i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					System.out.println("Finish thread " + Thread.currentThread().threadId() + ": " + i);
				});
	}



	@SneakyThrows
	public static void parallelPooledTest() {

		List<Integer> list = IntStream.range(1, 17).boxed().toList();
		ForkJoinPool pool = new ForkJoinPool(16);

		pool.submit(() ->
				list.parallelStream()
						.forEach(i -> {
							System.out.println("Start  " + Thread.currentThread().threadId() + ": " + i);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
							System.out.println("Finish " + Thread.currentThread().threadId() + ": " + i);
						})
		).get();
	}


	public static void spliteratorTryAdvanceTest() {
		List<String[]> values = Stream.generate(() -> new String[]{"Value"})
				.limit(10)
				.toList();

		Spliterator<String[]> spliterator = values.stream().spliterator();
		while (spliterator.tryAdvance(value -> value[0] += "__updated"));

		values.forEach(value -> System.out.println(value[0]));
	}


	public static void spliteratorTrySplitTest() {
		List<String[]> values = Stream.generate(() -> new String[]{"Value"})
				.limit(10)
				.toList();

		Spliterator<String[]> spliterator = values.stream().spliterator();
		Spliterator<String[]> spliterator2 = spliterator.trySplit();

		StreamSupport.stream(spliterator2, false)
				.findFirst().ifPresent(value ->
						System.out.printf("Value taken from spliterator2: %s%n", value[0])
				);

		System.out.printf("Size of spliterator: %d%n", spliterator.estimateSize());
		System.out.printf("Size of spliterator2: %d%n%n", spliterator2.estimateSize());

		spliterator.forEachRemaining((value -> value[0] += "_updated"));

		values.forEach(value -> System.out.println(value[0]));
	}

}

