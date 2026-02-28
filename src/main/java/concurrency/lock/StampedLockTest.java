package concurrency.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;


@Slf4j
public class StampedLockTest {

	public static void main(String[] args) throws InterruptedException {
		test();
	}


	public strictfp static void test() throws InterruptedException {

		Holder holder = new Holder();

		Thread t1 = new Thread(() -> {
			holder.set("Hello");
//			holder.get();
//			Object value = holder.getOptimistic();
//			log.info("Value from thread " + Thread.currentThread().getName() + ": " + value);
		}, "Thread_1");

		t1.start();

//		Object value = holder.getOptimistic(stamp -> {t1.start();});
		Object value = holder.get();
		log.info("Value from thread " + Thread.currentThread().getName() + ": " + value);
	}


	public static class Holder {
		private StampedLock lock = new StampedLock();
		private Object data = "DUMP_DATA";

		public Object getOptimistic(Consumer<Long> consumer) {
			log.info("Trying to acquire an Optimistic Read Lock by thread " + Thread.currentThread().getName());
			long stamp = lock.tryOptimisticRead();
			if (stamp == 0) {
				log.info("Could not acquire Optimistic Read Lock in thread " + Thread.currentThread().getName());
				return null;
			}
			try {
				if (consumer != null) {
					consumer.accept(stamp);
				}
				log.info("Acquired Optimistic Read Lock by thread " + Thread.currentThread().getName() + ": " + stamp);
				Thread.sleep(1000);
				boolean isValid = lock.validate(stamp);
				log.info("Optimistic Read Lock is valid by thread " + Thread.currentThread().getName() + ": " + stamp + " = " + isValid);
				Thread.sleep(2000);
				return data;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				log.info("Releasing the Optimistic Read Lock by thread " + Thread.currentThread().getName() + ": " + stamp);
				boolean unlock = lock.tryUnlockRead();
				log.info("Optimistic Read Lock is released by thread " + Thread.currentThread().getName() + ": " + unlock);
			}
		}


		public Object get() {
			log.info("Trying to acquire a Read Lock by thread " + Thread.currentThread().getName());
			long stamp = lock.readLock();
			try {
//				lock.readLock(); // Deadlock will happen in this case as StampedLock is not re-entrant

				log.info("Acquired Read Lock by thread " + Thread.currentThread().getName() + ": " + stamp);
				Thread.sleep(3000); // do something with dara
				return data;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				log.info("Releasing the Read Lock by thread " + Thread.currentThread().getName() + ": " + stamp);
				lock.unlockRead(stamp);
			}
		}


		public void set(Object value) {
			log.info("Trying to acquire a Write Lock by thread " + Thread.currentThread().getName());
			long stamp = lock.writeLock();
			try {
				log.info("Acquired Write Lock by thread " + Thread.currentThread().getName() + ": " + stamp);
				Thread.sleep(3000);
				this.data = value;
			} catch (InterruptedException e) {
				throw new RuntimeException("", e);
			} finally {
				log.info("Releasing the Write Lock by thread " + Thread.currentThread().getName() + ": " + stamp);
				lock.unlockWrite(stamp);
			}
		}
	}
}
