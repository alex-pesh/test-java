package concurrency.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Phaser;


/**
 * Phaser предоставляет возможность нескольким потокам выполнять несколько этапов (phases) и ожидать их выполнения соседними потоками.
 * <p> Позволяет динамически добавлять участников.
 * <p> Применяется при поэтапных (phased) вычислениях, где:
 * <ul>
 * 	<li> число потоков может меняться
 * 	<li> вычисление идёт шагами
 * 	<li> нужен контроль завершения
 * </ul>
 * <p> Например:
 * <li> фаза 0 — загрузка
 * <li> фаза 1 — обработка
 * <li> фаза 2 — агрегация
 */
@Slf4j
public class PhaserTest {

	public static void main(String[] args) {
		test();
	}


	public static void test() {

		Phaser phaser = new Phaser() {
			private final int phasesCount = 3;

			// Вызывается перед переходом к следующей фазе, после прихода всех parties
			// Когда onAdvance() возвращает true, то phaser.isTerminated() == true
			// и все ждущие потоки на arriveAndAwaitAdvance() немедленно возвращаются.
			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				log.info("Phase {} completed, parties={}", phase, registeredParties);
				return phase >= phasesCount - 1 || super.onAdvance(phase, registeredParties);
			}
		};


		Thread t1 = new Thread(new Task(phaser), "Thread_1");
		Thread t2 = new Thread(new Task(phaser), "Thread_2");
		Thread t3 = new Thread(new Task(phaser), "Thread_3");

		t1.start();
		t2.start();
		t3.start();

/*		try {
			Thread.sleep(3000);
			log.info("Forcibly terminate all");
			phaser.forceTermination();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}*/

	}


	private static class Task implements Runnable {

		private final int id;
		private final Phaser phaser;

		private Task(Phaser phaser) {
			this.phaser = phaser;
			phaser.register(); 	// Можно сразу создать Phaser(int Phaser) и не регистрировать каждый поток, если их количество известно
			this.id = phaser.getRegisteredParties();
		}

		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();

			while (!phaser.isTerminated()) {
				int currentPhase = phaser.getPhase();
				log.info(threadName + " started phase " + currentPhase);
				try {
					Thread.sleep(this.id * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				boolean hasOthers = !phaser.isTerminated() && (phaser.getUnarrivedParties() - 1 > 0);
				log.info(threadName + " finished phase " + currentPhase + (hasOthers ? " and awaiting for others " : ""));
				phaser.arriveAndAwaitAdvance();

//				int phase = phaser.arrive();
//				log.info(threadName + " finished phase " + phase);

			}
		}
	}
}
