package concurrency;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ReferenceTest {

	public static void main(String[] args) {
		test();
	}


	static class NamedWeakReference<T> extends WeakReference<T> {
		private String name;

		public NamedWeakReference(String name, T referent, ReferenceQueue<? super T> queue) {
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
		Reference<List<Integer>> listRef_1, listRef_2;

		Thread t1 = new Thread(() -> {
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
		}, "Signal_thread");

		t1.setDaemon(true);
		t1.start();


		{
			List<Integer> list_1 = new ArrayList<>();
			listRef_1 = new NamedWeakReference<>("ListRef_1", list_1, queue);

			for (int i = 0; i < 30_000_000; i++) {
				if (listRef_1.refersTo(list_1)) {
					list_1.add(i);
				} else {
					System.out.println("Warn: List_1 is collected at " + i);
					break;
				}
			}

		}

		List<Integer> list_2 = new ArrayList<>();
		listRef_2 = new NamedWeakReference<>("ListRef_2", list_2, queue);

		for (int i = 0; i < 30_000_000; i++) {
			if (listRef_2.refersTo(list_2)) {
				list_2.add(i);
			} else {
				System.out.println("Warn: List_2 is collected at " + i);
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
