package concurrency.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @{code CyclicBarrier} используется в сценариях, когда нужно, чтобы {@code N} потоков ждали завершения выполнения друг друга.
 * При вызове {@code await()} поток ждет:
 * <li> пока не будет вызван {@code await()} еще {@code N-1} раз
 * <li> или вызван {@code reset()} — в этом случае будет брошен {@code BrokenBarrierException}
 * <li> или {@code interrupt()} текущего потока или других, ждущих на {@code await()}
 */
@Slf4j
public class CyclicBarrierTest {

	public static void main(String[] args) {
		test_1();
	}




	public static void test_1() {

		class Task implements Runnable {
			CyclicBarrier barrier;
			String name;
			long delay;

			Task(CyclicBarrier c, String n) {
				barrier = c;
				name = n;
			}

			Task(CyclicBarrier c, String n, long d) {
				this(c, n);
				delay = d;
			}


			public void run() {
				try {
					Thread.sleep(delay);
					log.info(name + " done (waiting for others)");

					// В случае таймаута на каком-то из потоков, другие потоки, находящиеся в await() получат BrokenBarrierException
					// и barrierAction не будет выполнен.
//					barrier.await(delay, TimeUnit.MILLISECONDS);

					barrier.await();

					log.info("Barrier is reached for thread: " + name);

//				} catch (TimeoutException e) {
//					log.info("Time is out for thread: " + name);
				} catch (InterruptedException e) {
					log.info("Thread is interrupted: " + name);
				} catch (BrokenBarrierException e) {
					log.info("Barrier is reset for thread: " + name);
				}
			}
		}


		final Runnable barrierAction = () -> log.info("Barrier Reached!");

		CyclicBarrier cb = new CyclicBarrier(5, barrierAction);

		log.info("Starting");

		ThreadGroup tg = new ThreadGroup("CyclicTaskGroup");
		new Thread(tg, new Task(cb, "A", 3000), "Thread_A").start();
		new Thread(tg, new Task(cb, "B", 5000), "Thread_B").start();
		new Thread(tg, new Task(cb, "C", 500), 	"Thread_C").start();
		new Thread(tg, new Task(cb, "D", 500), 	"Thread_D").start();
		new Thread(tg, new Task(cb, "E", 500), 	"Thread_E").start();
	}




	public static void test_2() {

		class Task implements Runnable {
			private final CyclicBarrier barrier;

			public Task(CyclicBarrier barrier) {
				this.barrier = barrier;
			}

			@Override
			public void run() {
				try {
					log.info(Thread.currentThread().getName() + " выполняет работу.");
					Thread.sleep(1000);  // Симуляция работы
					log.info(Thread.currentThread().getName() + " достиг барьера.");
					barrier.await();  // Ожидание остальных потоков
					log.info(Thread.currentThread().getName() + " продолжает выполнение.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		int numThreads = 3;
		CyclicBarrier barrier = new CyclicBarrier(numThreads, () -> {
			try {
				log.info("Все потоки достигли барьера. Выполняем действие.");
				Thread.sleep(3000);  // Симуляция работы
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		for (int i = 0; i < numThreads; i++) {
			new Thread(new Task(barrier)).start();
		}
	}

}
