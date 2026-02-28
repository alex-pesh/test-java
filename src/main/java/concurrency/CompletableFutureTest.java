package concurrency;

import org.slf4j.Logger;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * https://habr.com/ru/companies/otus/articles/818955/
 * https://habr.com/ru/articles/784144/#inside
 *
 */

public class CompletableFutureTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(CompletableFutureTest.class);

	public static void main(String[] args) {
		deferredAsyncTest();
	}


	public static void deferredAsyncTest() {
		Supplier<CompletableFuture<String>> provider = () -> {

			// Сначала строится цепочка обратных вызовов, только затем вызывается обработка.
			// помогает избежать задержек на перенос выполнения кода обработчиков между потоками в случаях, когда результат
			// первой задачи приходит раньше, чем построится цепочка
			CompletableFuture<String> start = new CompletableFuture<>();
			CompletableFuture<String> end = start
					.thenCompose(data -> CompletableFuture.completedFuture(data + "_updated"))
					.thenApply(data -> data);
			start.completeAsync(() -> "Value");
			return end;
		};

		try {
			log.info(provider.get().get());
		} catch (Exception e) {
			throw new RuntimeException("", e);
		}
	}


	/**
	 * Реализация <code>CompletableFuture.cancel(boolean mayInterruptIfRunning)</code> игнорирует параметр <code>mayInterruptIfRunning</code>,
	 * поэтому для прерывания потоков можно использовать <code>ExecutorService.shutdownNow()</code>
	 */
	public static void testCancel() {
		try (ExecutorService exec = Executors.newFixedThreadPool(10)) {

			CompletableFuture<String> source = CompletableFuture.supplyAsync(() -> {
				try {
					log.info("Task 1 is started");
					Thread.sleep(1000);
					log.info("Task 1 is finished");
				} catch (InterruptedException _) {
					log.info("Task 1 is interrupted");
				}

				return "Task_1_result";
			}, exec);

			source.thenApply(data -> {
				try {
					log.info("Task 2 is started");
					Thread.sleep(3000);
					log.info("Task 2 is finished");
				} catch (InterruptedException _) {
					log.info("Task 2 is interrupted");
				}

				return data + " Task_2_result";

			}).thenApply(data -> {
				try {
					log.info("Task 3 is started");
					Thread.sleep(3000);
					log.info("Task 3 is finished");
				} catch (InterruptedException _) {
					log.info("Task 3 is interrupted");
				}

				return data + " Task_3_result";
			});


			try {
				Thread.sleep(1500);
			} catch (InterruptedException _) {}

			source.cancel(true);
			log.info("Source is canceled, but task 2 is still running");

			log.info("Shutting down ExecutorService");
			exec.shutdownNow();

			log.info("Result: " + source);

		}
	}


	public static void calculateAsyncTest() {

//		Object result = calculateAsync().get();
//		log.info(result);

		calculateAsync().thenApply((data) -> {
			log.info(data);
			return data + " World";
		}).handle((data, t) -> {
			log.info(data);
			return data;
		});

	}

	private static CompletableFuture<String> calculateAsync() {
		CompletableFuture<String> completableFuture = new CompletableFuture<>();

		try (ExecutorService executor = Executors.newCachedThreadPool()) {
			executor.submit(() -> {
				Thread.sleep(1000);
				completableFuture.complete("Hello");
				return null;
			});
		}

		return completableFuture;
	}


	// https://habr.com/ru/companies/otus/articles/818955/

	/**
	 * Метод thenCompose() используется для объединения двух зависимых задач. Когда первая задача завершится,
	 * thenCompose() используется для запуска следующей задачи, используя результат первой.
	 * Это предотвращает некоторую вложенность.
	 */
	public static void thenComposeTest() {

		CompletableFuture<Double> result = getUserDetail("123")
				.thenCompose(user -> getCreditRating(user));

		result.thenAccept(rating -> {
			log.info("Результат: " + rating);
		});

		try {
			TimeUnit.MINUTES.sleep(20);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}

//		ForkJoinPool.commonPool().awaitQuiescence(1, TimeUnit.MINUTES);
	}

	private static CompletableFuture<User> getUserDetail(String userId) {
		ExecutorService exec = Executors.newFixedThreadPool(10);

		return CompletableFuture.supplyAsync(() -> {
			// запрос к удаленному сервису для получения данных пользователя
//			User user =  UserService.getUserDetails(userId);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			User user = new User(userId);
			log.info("Загружены данные пользователя : " + user.userId);

			return user;
		}, exec);
	}

	private static CompletableFuture<Double> getCreditRating(User user) {
		return CompletableFuture.supplyAsync(() -> {
			// запрос к другому сервису для получения кредитного рейтинга
			//return CreditRatingService.getCreditRating(user);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			double result = 0.54;
			log.info("Вычислен кредитный рейтинг: " + result);

			return result;
		});
	}

	private static record User (String userId) {}



	/**
	 * Метод thenCombine() используется для объединения результатов двух независимых задач.
	 * <p>Запускает обе задачи параллельно и комбинирует их результаты, когда обе задачи завершены.
	 */
	public static void thenCombineTest() {

		try (ExecutorService exec = Executors.newFixedThreadPool(10)) {

			CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}

				double result = 65.0;
				log.info("Вес вычислен: " + result);
				return result;
			}, exec);

			CompletableFuture<Double> heightInCmFuture = CompletableFuture.supplyAsync(() -> {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}

				double result = 175.0;
				log.info("Рост вычислен: " + result);
				return result;
			}, exec);

			CompletableFuture<Double> bmiFuture = weightInKgFuture.thenCombine(heightInCmFuture, (weight, height) -> {
				double heightInMeters = height / 100;
				return weight / (heightInMeters * heightInMeters);
			});

			bmiFuture.thenAccept(bmi -> {
				log.info("Индекс массы тела: " + bmi);
			});
		}
	}


	/**
	 * Паттерн fan-out/fan-in позволяет выполнять несколько задач параллельно и затем объединять их результаты.
	 * Это можно реализовать с помощью методов allOf() и anyOf().
	 */
	public static void allOfTest() {
		CompletableFuture<Void> future1, future2;

		try (ExecutorService exec = Executors.newFixedThreadPool(10)) {

			future1 = CompletableFuture.runAsync(() -> {
				log.info("Задача 1 выполнена");
			}, exec);

			future2 = CompletableFuture.runAsync(() -> {
				log.info("Задача 2 выполнена");
			}, exec);

			CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);

			combinedFuture.thenRun(() -> {
				log.info("Все задачи выполнены");
			});
		}

	}

}
