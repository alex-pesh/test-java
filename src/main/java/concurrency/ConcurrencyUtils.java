package concurrency;

import java.util.function.Supplier;

public class ConcurrencyUtils {

	/**
	 * Wait for a condition using the monitor of a given object.
	 */
	public static void waitObject(Object obj, Supplier<Boolean> condition, long deadlineMs) throws InterruptedException {
		synchronized (obj) {
			while (true) {
				if (condition.get())
					return;

				long currentTimeMs = System.currentTimeMillis();
				if (currentTimeMs >= deadlineMs) {
					throw new RuntimeException("Condition not satisfied before deadline");
				}

				obj.wait(deadlineMs - currentTimeMs);
			}
		}
	}
}
