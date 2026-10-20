package concurrency.sync;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

	public static void main(String[] args) {
		test_1();
	}

	/**
	 * Главный поток ждет, пока оба потока завершат выполнение
	 */
	public static void test_1() {

		CountDownLatch latch = new CountDownLatch(2);
		Thread t1 = new Thread(new CountDownTask(latch, 1000), "Thread-1");
		Thread t2 = new Thread(new CountDownTask(latch, 4000), "Thread-2");

		t1.start();
		t2.start();

		try {
			System.out.println("Awaiting for latch releases: " + latch.getCount());

			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}

		System.out.println("Latch released");
	}


	/**
	 * Оба потока ждут, пока будет дан старт на выполнение главным потоком
	 */
	public static void test_2() {
		CountDownLatch latch = new CountDownLatch(1);
		Thread t1 = new Thread(new AwaitTask(latch), "Thread-1");
		Thread t2 = new Thread(new AwaitTask(latch), "Thread-2");

		t1.start();
		t2.start();

		try {
			Thread.sleep(2000);
			System.out.println("Latch releasing");
			latch.countDown();
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}
	}



	private static class CountDownTask implements Runnable {
		private CountDownLatch latch;
		private long timeout;

		public CountDownTask(CountDownLatch latch, long timeout) {
			this.latch = latch;
			this.timeout = timeout;
		}

		@Override
		public void run() {
			if (latch.getCount() > 0) {
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
					throw new RuntimeException("", e);
				}
				latch.countDown();
				System.out.println("Latch decremented by thread: " + Thread.currentThread().getName());
			}
		}
	}


	private static class AwaitTask implements Runnable {
		private CountDownLatch latch;

		public AwaitTask(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				System.out.println("Thread is waiting for latch release: " + Thread.currentThread().getName());
				latch.await();
				System.out.println("Thread finishing: " + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			}
		}
	}

}
