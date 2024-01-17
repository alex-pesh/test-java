package concurrency.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {


	public static void test() throws InterruptedException {

		Holder holder = new Holder();

		Thread t1 = new Thread(() -> {
//			holder.set("Hello");
			System.out.println("Value from thread" + Thread.currentThread().getName() + ": " + holder.get());
		}, "Thread_1");

		t1.start();

		Thread.sleep(100);
		holder.set("olleH");

		System.out.println("Value from thread" + Thread.currentThread().getName() + ": " + holder.get());
	}


	public static class Holder {
		private ReadWriteLock rwLock = new ReentrantReadWriteLock();
		private Object data;

		public Object get() {
			rwLock.readLock().lock();
			try {
				System.out.println("Getting value by thread " + Thread.currentThread().getName());
				Thread.sleep(10000);
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
				System.out.println("Setting value by thread " + Thread.currentThread().getName());
				Thread.sleep(5000);

				this.data = value;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				rwLock.writeLock().unlock();
			}
		}
	}
}
