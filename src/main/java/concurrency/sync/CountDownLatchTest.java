package concurrency.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class CountDownLatchTest {

	public static void test() {

		CountDownLatch latch = new CountDownLatch(2);

		Thread t1 = new Thread(() -> {
			while (latch.getCount() > 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException("", e);
				}
				latch.countDown();
			}
		});

		t1.start();

		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}

		System.out.println("Latch released");

	}

}
