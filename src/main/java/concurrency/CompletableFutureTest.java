package concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

	public static void test() throws Exception {

//		Object result = calculateAsync().get();
//		System.out.println(result);

		calculateAsync().thenApply((data) -> {
			System.out.println(data);
			return data + " World";
		}).handle((data, t) -> {
			System.out.println(data);
			return data;
		});

	}

	static CompletableFuture<String> calculateAsync() throws InterruptedException {
		CompletableFuture<String> completableFuture = new CompletableFuture<>();

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.submit(() -> {
			Thread.sleep(1000);
			completableFuture.complete("Hello");
			return null;
		});

		executor.shutdown();

		return completableFuture;
	}

}
