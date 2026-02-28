package concurrency.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LockConditionTest {

	public static void main(String[] args) {
		test();
	}


	public static void test() {

		Lock lock = new ReentrantLock(true);
		Condition condition = lock.newCondition();

		Thread t1 = new Thread(() -> {
			while (true) {
				log.info(Thread.currentThread().getName() + " is waiting for lock");
				lock.lock();
				try {
					log.info(Thread.currentThread().getName() + " taken the lock and doing something");
					Thread.sleep(2000);
					log.info(Thread.currentThread().getName() + " is done and waiting for signal");
					condition.await();
					log.info(Thread.currentThread().getName() + " woken");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}, "Thread_1");

		Thread t2 = new Thread(() -> {
			while (true) {
				log.info(Thread.currentThread().getName() + " is waiting for lock");
				lock.lock();
				try {
					log.info(Thread.currentThread().getName() + " taken the lock and signaling");
					condition.signal();
					log.info(Thread.currentThread().getName() + " has signaled and doing something");
					Thread.sleep(2000);
					log.info(Thread.currentThread().getName() + " is done");
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
