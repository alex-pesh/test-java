package algos.misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * https://matthewmacfarquhar.medium.com/mastering-mapreduce-a-step-by-step-java-tutorial-for-big-data-processing-47e1bd96d6e2
 *
 */
public class MapReduceTest {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		// MAP
		String[] filePaths = {
				"src/main/resources/logs/log_1.txt",
				"src/main/resources/logs/log_2.txt",
				"src/main/resources/logs/log_3.txt",
				"src/main/resources/logs/log_4.txt",
				"src/main/resources/logs/log_5.txt",
				"src/main/resources/logs/log_6.txt",
				"src/main/resources/logs/log_7.txt",
				"src/main/resources/logs/log_8.txt",
				"src/main/resources/logs/log_9.txt",
				"src/main/resources/logs/log_10.txt",
		};

		ExecutorService mapExecutor = Executors.newFixedThreadPool(10);
		List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
		for (String filePath : filePaths) {
			MapCallable mapCallable = new MapCallable(filePath);
			Future<Map<String, Integer>> future = mapExecutor.submit(mapCallable);
			futureList.add(future);
		}
		mapExecutor.shutdown();
		mapExecutor.awaitTermination(1, TimeUnit.SECONDS);

		// SHUFFLE
		Map<String, List<Integer>> shuffledMetrics = new HashMap<>();
		for (Future<Map<String, Integer>> mapFuture : futureList) {
			Map<String, Integer> mappedMetrics = mapFuture.get();
			mappedMetrics.forEach((key, value) -> {
				List<Integer> metricCountList = shuffledMetrics.getOrDefault(key, new ArrayList<>());
				metricCountList.add(value);
				shuffledMetrics.put(key, metricCountList);
			});
		}

		// REDUCE
		ExecutorService reduceExecutor = Executors.newFixedThreadPool(3);
		List<Future<Map.Entry<String, Integer>>> futureReducerList = new ArrayList<>();
		shuffledMetrics.forEach((key, value) -> {
			ReduceCallable reduceCallable = new ReduceCallable(key, value);
			Future<Map.Entry<String, Integer>> futureReducer = reduceExecutor.submit(reduceCallable);
			futureReducerList.add(futureReducer);
		});
		reduceExecutor.shutdown();
		reduceExecutor.awaitTermination(1, TimeUnit.SECONDS);

		// GET RESULTS
		Map<String, Integer> resultMap = new HashMap<>();
		for (Future<Map.Entry<String, Integer>> futureEntry : futureReducerList) {
			Map.Entry<String, Integer> entry = futureEntry.get();
			resultMap.put(entry.getKey(), entry.getValue());
		}

		System.out.println(resultMap);
	}


	static class MapCallable implements Callable<Map<String, Integer>> {
		private final String filePath;
		private final Map<String, Integer> logMap;

		public MapCallable(String filePath) {
			this.filePath = filePath;
			this.logMap = new HashMap<>();
		}

		@Override
		public Map<String, Integer> call() throws Exception {
			try (BufferedReader reader = new BufferedReader(new FileReader(this.filePath))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String key = line.trim();
					int valueToPut = this.logMap.getOrDefault(key, 0) + 1;
					this.logMap.put(key, valueToPut);
				}
				return this.logMap;
			}
		}
	}


	static class ReduceCallable implements Callable<Map.Entry<String, Integer>> {
		private final String metricName;
		private final List<Integer> metricCounts;

		public ReduceCallable(String metricName, List<Integer> metricCounts) {
			this.metricName = metricName;
			this.metricCounts = metricCounts;
		}

		@Override
		public Map.Entry<String, Integer> call() throws Exception {
			int total = 0;
			for (Integer metricCount : metricCounts) {
				total += metricCount;
			}

			return Map.entry(this.metricName, total);
		}
	}

}
