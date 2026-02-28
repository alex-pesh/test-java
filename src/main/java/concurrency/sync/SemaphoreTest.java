package concurrency.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

@Slf4j
public class SemaphoreTest {


	public static void test() {

		Semaphore semaphore = new Semaphore(1, true);

		Runnable runnable = () -> {
			while (true) {
				try {
					log.info(Thread.currentThread().getName() + " is trying to acquire the semaphore");
					semaphore.acquire();
					log.info(Thread.currentThread().getName() + " acquired the semaphore");
					Thread.sleep(2000);
					log.info(Thread.currentThread().getName() + " released the semaphore");
					semaphore.release();
				} catch (InterruptedException e) {
					throw new RuntimeException(" ", e);
				}
			}
		};

		Thread t1 = new Thread(runnable, "Thread_1");
		Thread t2 = new Thread(runnable, "Thread_2");

		t1.start();
		t2.start();

	}


	public static void main(String[] args) {
		SemaphoreTest.test();
	}

}
