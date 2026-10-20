package core;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;

public class StructuredTaskScopeTest {

	public static void main(String[] args) {
		test();
	}


	public static void test() {

/*
		 Структурированный параллелизм. Жизненный цикл всех дочерних задач строго ограничен
		 областью видимости (блоком кода), с автоматической отменой и обработкой ошибок.
		 Подходит для I/O-ориентированных задач.
		 Например, микросервисов: параллельные вызовы нескольких API, запросы к базам данных, чтение файлов.
		 Задачи запускаются внутри блока try-with-resources. Как только блок завершается, все незавершенные задачи автоматически отменяются.
		 По умолчанию использует виртуальные потоки (Virtual Threads). Это позволяет создавать сотни тысяч легковесных задач, не нагружая операционную систему.
*/

		List<Callable<String>> tasks = List.of(
				() -> { return "Result 1"; },
				() -> { return "Result 2"; }
		);

		try (StructuredTaskScope<String> scope = new StructuredTaskScope.ShutdownOnSuccess()) {

			List<StructuredTaskScope.Subtask<String>> subtasks = tasks.stream().map(scope::fork).toList();

			scope.join();

			subtasks.stream().forEach(subtask -> {
				System.out.printf("Subtask %s: %s%n", subtask.state(), subtask.get());
			});

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
