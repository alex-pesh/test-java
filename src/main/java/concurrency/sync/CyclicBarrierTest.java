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
		CyclicBarrier cb = new CyclicBarrier(3, () -> System.out.println("Barrier Reached!"));

		System.out.println("Starting");

		new Thread(new Task(cb, "A")).start();
		new Thread(new Task(cb, "B")).start();
		new Thread(new Task(cb, "C")).start();
	}

}
