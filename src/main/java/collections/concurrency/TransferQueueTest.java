package collections.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class TransferQueueTest {

	public static void main(String[] args) {
		test();
	}

	public static void test() {

//		final SynchronousQueue<String> queue = new SynchronousQueue<>(true);
		final TransferQueue<String> queue = new LinkedTransferQueue<>();

		Runnable task = ()  -> {
			String tName = Thread.currentThread().getName();
			while (true) {
				try {
					String value = "Hello from " + tName;
					log.info(tName + " transferring element: " + value);
					queue.put(value);
//					queue.transfer(value);
					log.info(tName + " is woken up");

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};


		Thread t1 = new Thread(task, "Tread_1");
		Thread t2 = new Thread(task, "Tread_2");

		t1.setDaemon(true);
		t2.setDaemon(true);
		t1.start();
		t2.start();

		LockSupport.parkNanos(2000_000_000L);

		for (int i = 0; i < 4; i++) {
			try {
				System.out.println();
				log.info(Thread.currentThread().getName() + " requested element");
				String value = queue.take();
				log.info(Thread.currentThread().getName() + " taken element: " + value);
				LockSupport.parkNanos(2000_000_000L);

			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
