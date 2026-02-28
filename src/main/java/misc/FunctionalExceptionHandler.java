package misc;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * Прием обработки исключения в функциональном пайпе: конфликтный код оборачивается в union-объект Try, который может
 * принимать значения Success или Failure. Затем он передается вниз по цепочке.
 */
public class FunctionalExceptionHandler {

	public static void main(String[] args) {

		List<String> itemNames =
		Stream.of("C001", "C002", "C003", "C004", "C005")
				.map(code -> Try.of(() -> loadItem(code)))
				.peek(itemTry -> System.out.println("Загруженный элемент: " + itemTry))
				.map(itemTry -> itemTry.map(item -> item.name))
				.map(Try::unwrap)
				.toList();

		System.out.println(itemNames);
	}


	private static Item loadItem(String code) throws Exception {
		if (code.equals("C003")) {
			throw new Exception("Item not found: " + code);
		}

		return new Item(code, "Item__" + code);
	}


	public static class Item {
		private String code;
		private String name;

		public Item(String code, String name) {
			this.code = code;
			this.name = name;
		}

		@Override
		public String toString() {
			return STR."{Code: \{code}; Name: \{name}";
		}
	}


	public sealed interface Try<T> permits Success, Failure {
		T getResult();
		Throwable getError();

		static <T> Try<T> of(Callable<T> callable) {
			try {
				return new Success<>(callable.call());
			} catch (Throwable throwable) {
				return new Failure<>(throwable);
			}
		}

		<R> Try<R> map(Function<T, R> mapper);

		default T unwrap() {
			return null;
		}
	}


	public record Success<T>(T result) implements Try<T> {
		@Override
		public T getResult() {
			return result;
		}

		@Override
		public Throwable getError() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <R> Try<R> map(Function<T, R> mapper) {
			return Try.of(() -> mapper.apply(result));
		}

		@Override
		public T unwrap() {
			return result;
		}
	}


	public record Failure<T>(Throwable throwable) implements Try<T> {
		@Override
		public T getResult() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Throwable getError() {
			return throwable;
		}

		@Override
		public <R> Try<R> map(Function<T, R> mapper) {
			return new Failure<>(throwable);
		}
	}

}
