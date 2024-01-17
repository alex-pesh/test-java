package concurrency.sync;

import java.util.concurrent.Phaser;

public class PhaserTest {

	public static void test() {

		Phaser phaser = new Phaser() {
			private final int phasesCount = 3;

			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase >= phasesCount - 1 || super.onAdvance(phase, registeredParties);
			}
		};

		Thread t1 = new Thread(new Task(phaser), "Thread_1");
		Thread t2 = new Thread(new Task(phaser), "Thread_2");

		t1.start();
		t2.start();
	}


	private static class Task implements Runnable {

		private final int id;
		private final Phaser phaser;

		private Task(Phaser phaser) {
			this.phaser = phaser;
			phaser.register();
			this.id = phaser.getRegisteredParties();
		}

		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();

			while (!phaser.isTerminated()) {
				System.out.println(threadName + " started phase " + phaser.getPhase());
				try {
					Thread.sleep(this.id * 1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println(threadName + " finished phase " + phaser.getPhase() + " and awaiting for others ");

				phaser.arriveAndAwaitAdvance();

				System.out.println(threadName + " woken up");

			}
		}
	}
}
