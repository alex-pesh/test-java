package collections.concurrency;

import java.time.Duration;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * {@link DelayQueue} - обертка над {@linkplain java.util.PriorityQueue}, где в качестве natural ordering
 * предоставляется возвращаемое значение элементов {@link Delayed#getDelay(TimeUnit)}
 * <p>
 * Методы {@link DelayQueue#poll()}, {@link DelayQueue#poll(long, TimeUnit)}, {@link DelayQueue#take()} и {@link DelayQueue#remove()}
 * переопределены с учетом истечения времени, возвращаемого методом {@link Delayed#getDelay(TimeUnit)} элемента
 */
public class DelayQueueTest {

	public static void main(String[] args) throws InterruptedException {

		DelayQueue<DelayedImpl> dq = new DelayQueue<>();
		dq.add(new DelayedImpl("First", Duration.ofSeconds(5)));
		dq.add(new DelayedImpl("Second", Duration.ofSeconds(3)));
		dq.add(new DelayedImpl("Third", Duration.ofSeconds(2)));

		DelayedImpl di;
		while(!dq.isEmpty()) {
			di = dq.take();
			System.out.println(di);
		}
	}


	static class DelayedImpl implements Delayed {
		String name;
		long time;

		public DelayedImpl(String name, Duration duration) {
			this.name = name;
			this.time = System.nanoTime() + duration.toNanos();
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(time - System.nanoTime(), NANOSECONDS);
		}

		@Override
		public int compareTo(Delayed other) {
			long diff = getDelay(NANOSECONDS) - other.getDelay(NANOSECONDS);
			return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
		}

		@Override
		public String toString() {
			return name;
		}
	}

}
