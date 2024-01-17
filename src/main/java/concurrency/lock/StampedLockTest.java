package concurrency.lock;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTest {

	public strictfp static void test() throws InterruptedException {

		Holder holder = new Holder();

		Thread t1 = new Thread(() -> {
			holder.set("Hello");
//			Object value = holder.getOptimistic();
//			System.out.println("Value from thread " + Thread.currentThread().getName() + ": " + value);
		}, "Thread_1");

		t1.start();

		Thread.sleep(100);

//		holder.set("olleH");
		Object value = holder.getOptimistic();
		System.out.println("Value from thread" + Thread.currentThread().getName() + ": " + value);
	}


	public static class Holder {
		private StampedLock lock = new StampedLock();
		private Object data;

		public Object getOptimistic() {
			long stamp = lock.tryOptimisticRead();
			if (stamp == 0) {
				System.out.println("Could not acquire optimistic lock in thread " + Thread.currentThread().getName());
				return null;
			}
			try {
				System.out.println("Getting value by thread " + Thread.currentThread().getName());
				Thread.sleep(10000);
				return data;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				boolean unlock = lock.tryUnlockRead();
				System.out.println("Lock is released by thread " + Thread.currentThread().getName() + ": " + unlock);

			}
		}


		public Object get() {
			long stamp = lock.readLock();
			try {
//				lock.readLock(); // Deadlock will happen in this case as StampedLock is not re-entrant

				System.out.println("Getting value by thread " + Thread.currentThread().getName());
				Thread.sleep(10000);
				return data;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				System.out.println("Lock is  exclusively acquired: " + lock.validate(stamp));
				lock.unlockRead(stamp);
			}
		}


		public void set(Object value) {
			long stamp = lock.writeLock();
			try {
				System.out.println("Setting value by thread " + Thread.currentThread().getName());
				Thread.sleep(10000);

				this.data = value;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				lock.unlockWrite(stamp);
			}
		}
	}
}
