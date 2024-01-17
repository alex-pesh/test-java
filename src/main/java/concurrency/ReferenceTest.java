package concurrency;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ReferenceTest {

	static class NamedReference<T> extends WeakReference<T> {
		private String name;

		public NamedReference(String name, T referent, ReferenceQueue<? super T> queue) {
			super(referent, queue);
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}


	public static void test() {

		ReferenceQueue<List<Integer>> queue = new ReferenceQueue<>();
		Reference<List<Integer>> listRef = new NamedReference<>("ListRef_1", new ArrayList<>(), queue);
		Reference<List<Integer>> listRef2 = new NamedReference<>("ListRef_2", new ArrayList<>(), queue);

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Reference<? extends List<Integer>> listRef = queue.remove();
						System.out.println("Collected list: " + listRef);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException("", e);
					}
				}
			}
		}, "Signal_thread");

		t1.start();


		for (int i = 0; i < 100_000_000; i++) {
			List<Integer> list = listRef.get();
			if (list != null) {
				list.add(i);
			} else {
				System.out.println("Warn: List_1 is collected");
				break;
			}
		}

		for (int i = 0; i < 1000_000_000; i++) {
			List<Integer> list = listRef2.get();
			if (list != null) {
				list.add(i);
			} else {
				System.out.println("Warn: List_2 is collected");
				break;
			}
		}


		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}

//		System.gc();

	}

}
