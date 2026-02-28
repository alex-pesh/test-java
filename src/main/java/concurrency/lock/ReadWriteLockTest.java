package concurrency.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class ReadWriteLockTest {


	public static void main(String[] args) throws InterruptedException {
		test();
	}


	/**
	 * <ul>
	 * <li>Несколько потока могут одновременно читать.
	 * <li>Поток не может читать, пока какой-то другой пишет.
	 * <li>Поток не может писать, пока какой-то другой читает.
	 * </ul>
	 *
	 * @throws InterruptedException
	 */
	public static void test() throws InterruptedException {

		Holder holder = new Holder();

		Thread t1 = new Thread(() -> {
//			holder.set("Hello");
			Object value = holder.get();
			log.info("Value from thread " + Thread.currentThread().getName() + ": " + value);
		}, "Thread_1");

		t1.start();

//		Thread.sleep(1000);
//		holder.set("olleH");

//		Object value = holder.get();
//		log.info("Value from thread " + Thread.currentThread().getName() + ": " + value);
	}


	public static class Holder {
		private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
		private Object data;

		public Object get() {
			rwLock.readLock().lock();
			try {
				log.info("Getting value by thread " + Thread.currentThread().getName());
				Thread.sleep(5000);
				return data;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				rwLock.readLock().unlock();
			}
		}


		public void set(Object value) {
			rwLock.writeLock().lock();
			try {
				log.info("Setting value by thread " + Thread.currentThread().getName());
				Thread.sleep(1000);

				this.data = value;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				rwLock.writeLock().unlock();
			}
		}
	}
}
