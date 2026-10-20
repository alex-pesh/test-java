package concurrency.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Каждый раз при создании Condition создается отдельная очередь ожидания.
 * В AbstractQueuedSynchronizer (AQS) существует два типа очередей:
 * <li> Sync queue (очередь синхронизации) — модифицированная CLH-очередь, в которой потоки ожидают получения ресурса (lock).
 * Это двунаправленный список с полями prev/next и атомарными операциями CAS.
 * <li> Condition queue (очередь условия) — отдельная очередь для каждого объекта Condition. Именно она создается при вызове lock.newCondition().
 * Это простая очередь FIFO, без сложной логики CLH, Однонаправленная: связи осуществляются через поле nextWaiter.
 * Доступ к condition queue происходит без CAS, только когда поток удерживает эксклюзивную блокировку,
 * поэтому не нужны атомарные операции для поддержки конкурентного доступа
 * <p><br>
 * <b>Схема работы:</b>
 * <li> Поток захватил блокировку. Его узел в sync queue стал головой (head), при этом поле thread у этого узла обнуляется (setHead). Это просто узел-заглушка.
 * <li> Поток вызывает condition.await().
 * <li> Внутри await() вызывается newConditionNode(), который создаёт новый Node и и добавляет его в очередь условия.
 * <li> Исходный узел в sync queue (голова) никуда не перемещается. Он так и остаётся головой, пока другой поток не захватит
 * блокировку и не заменит её своим узлом.
 * <li> Поток паркуется (LockSupport.park), ожидая сигнала.
 * <li> Когда другой поток вызывает condition.signal(), срабатывает doSignal(), который берёт тот самый новый
 * узел из очереди условия и перемещает его в sync queue (присоединяет к хвосту через prev/next и меняет waitStatus).
 * <li> После этого поток, ожидавший в await(), просыпается и пытается снова захватить блокировку, используя уже этот узел в sync queue
 * </p>
 */
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
