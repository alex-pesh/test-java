package core;

import java.awt.*;

public class ScopedValueTest {

	private static ScopedValue<String> KEY = ScopedValue.newInstance();


	public static void main(String[] args) {
		new ScopedValueTest().test();
	}


	public void test() {
/*
		 При использовании ScopedValue происходит inversion of control,
		 т.е. ScopedValue запускает целевой метод предварительно проставив значение value в стек потока.
		 Затем внутри иерархии вызовов можно в любом методе получить установленное значение
		 Это делает ScopedValue полезным инструментом для сквозных задач (cross-cutting concerns), таких как:
		 - Передача идентификатора пользователя (User-ID) через всю цепочку микросервисного вызова внутри JVM.
		 - Передача токена аутентификации в логирующие компоненты.
		 - Передача настроек транзакции в DAO-слой, минуя сервисный слой.
*/
		ScopedValue.where(KEY, "X").run(this::doTest);



		ScopedValue.where(KEY, "A").run(() -> {
			System.out.println(KEY.get());

			ScopedValue.where(KEY, "B").run(() -> {
				System.out.println(KEY.get());

				ScopedValue.where(KEY, "C").run(() -> {
					System.out.println(KEY.get());
				});

				System.out.println(KEY.get());
			});

			System.out.println(KEY.get());
		});

	}

	private void doTest() {
		System.out.println(KEY.get());
	}

}
