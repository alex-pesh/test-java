package concurrency.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionTest {

	public static void test() {

		Lock lock = new ReentrantLock(true);
		Condition condition = lock.newCondition();

		Thread t1 = new Thread(() -> {
			while (true) {
				lock.lock();
				try {
					System.out.println(Thread.currentThread().getName() + " taken the lock and awaiting");
					condition.await();
					System.out.println(Thread.currentThread().getName() + " woken");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}, "Thread_1");

		Thread t2 = new Thread(() -> {
			while (true) {
				lock.lock();
				try {
					Thread.sleep(2000);
					System.out.println(Thread.currentThread().getName() + " taken the lock and signaling");
					condition.signal();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}, "Thread_2");

		t1.start();
		t2.start();

	}

}
