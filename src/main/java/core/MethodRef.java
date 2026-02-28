package core;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Consumer;

public class MethodRef {

	private static void invoke(Runnable r) {
		r.run();
	}

	private static void target() {
		new Exception().printStackTrace();
	}


	/**
	 * При использовании лямбды компилятором генерируется дополнительный метод lambda$lambda$0
	 */
	@Test
	public void lambda() throws Exception {
		invoke(() -> target());
	}

	@Test
	public void methodRef() {
		invoke(MethodRef::target);
	}



	private static interface Measurable {
		int length(String input);
	}

	public void methodRef_2() {

		Consumer<Object> c = Objects::requireNonNull;

		c.accept(null);

		Measurable m = String::length;
		System.out.println(m.length("ASD"));
	}

}
