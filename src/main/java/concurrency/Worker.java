package concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * Пример использования CountDownLatch в качестве механизма для управления жизненным циклом воркера,
 * периодически выполняющего задачу в цикле.
 *
 */

@Slf4j
public class Worker extends Thread {

	private final CountDownLatch shutdownLatch;
	private final long pollMs;


	public Worker(long pollMs) {
		this.pollMs = pollMs;
		this.shutdownLatch = new CountDownLatch(1);
	}


	@Override
	public void run() {
		while (shutdownLatch.getCount() > 0) {
			try {
				// Do some job
			} catch (Exception e) {
				throw new RuntimeException("Unexpected error", e);
			}

			try {
				log.debug("Waiting {} ms to check for changed.", this.pollMs);
				boolean shuttingDown = shutdownLatch.await(this.pollMs, TimeUnit.MILLISECONDS);
				if (shuttingDown) {
					return;
				}
			} catch (InterruptedException e) {
				log.error("Unexpected InterruptedException, ignoring: ", e);
			}
		}
	}


	public void shutdown() {
		shutdownLatch.countDown();
	}

}
