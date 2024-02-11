package concurrency.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	static class Task implements Runnable {
		CyclicBarrier barrier;
		String name;
		Task(CyclicBarrier c, String n) {
			barrier = c;
			name = n;
		}
		public void run() {
			System.out.println(name);
			try {
				barrier.await();
			} catch (BrokenBarrierException | InterruptedException e) {
				System.out.println(e);
			}
		}
	}


	public static void test() {
		CyclicBarrier cb = new CyclicBarrier(5, () -> System.out.println("Barrier Reached!"));

		System.out.println("Starting");

		ThreadGroup tg = new ThreadGroup("CyclicTaskGroup");
		new Thread(tg, new Task(cb, "A"), "Thread_A").start();
		new Thread(tg, new Task(cb, "B"), "Thread_B").start();
		new Thread(tg, new Task(cb, "C"), "Thread_C").start();
		new Thread(tg, new Task(cb, "D"), "Thread_D").start();
		new Thread(tg, new Task(cb, "E"), "Thread_E").start();

	}

}
