package react;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxTest {

	public static void main(String[] args) {
		test_1();
	}

	public static void test_1() {

		Flux
				.generate(
						() -> 1,
						(state, sink) -> {
							if (state >= 10) {
								sink.complete();
							} else {
								sink.next("Step: " + state);
							}

							return state + 1;
						}
				)
				.subscribe(System.out::println);
	}


	public static void test_2() throws InterruptedException {
		Disposable disposable = Flux
				.generate(sink -> {
					sink.next("Hello");
				})
				.delayElements(Duration.ofMillis(500))
				.take(5)
				.subscribe(System.out::println);

		// delayElements generates Schedulers (in fact wrapper on ScheduledThreadPoolExecutor) that uses daemon threads
		Thread.sleep(5000);
	}


//	https://www.youtube.com/watch?v=77-wOZs2nPE

	public static void test_3() throws InterruptedException {

		Flux
				.create(emitter -> {
//					emitter.onRequest()
				});

	}

}
