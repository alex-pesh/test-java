package concurrency.sync;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

	public static void test() {

		Semaphore semaphore = new Semaphore(1, true);

		Thread t1 = new Thread(() -> {
			while (true) {
				try {
					semaphore.acquire();
					System.out.println(Thread.currentThread().getName() + " acquired the semaphore");
					Thread.sleep(2000);
					System.out.println(Thread.currentThread().getName() + " released the semaphore");
					semaphore.release();
				} catch (InterruptedException e) {
					throw new RuntimeException(" ", e);
				}
			}
		}, "Thread_1");

		Thread t2 = new Thread(() -> {
			while (true) {
				try {
					semaphore.acquire();
					System.out.println(Thread.currentThread().getName() + " acquired the semaphore");
					Thread.sleep(2000);
					System.out.println(Thread.currentThread().getName() + " released the semaphore");
					semaphore.release();
				} catch (InterruptedException e) {
					throw new RuntimeException(" ", e);
				}
			}
		}, "Thread_2");

		t1.start();
		t2.start();

	}

}
