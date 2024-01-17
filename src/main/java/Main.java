import core.NumberRanges;
import instrument.InstrumentationAgent;
import misc.PrimesSearchTest;
import misc.StringOps;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class Main {

	interface I {
		void print();
	}

	public I create() {
		return () -> System.out.println("Hello");
	}

	private I i = this::create;

	public void test() {
		i.print();
	}


/*
	private static void doList(List<? super B> list) {
		list.add(new A(""));
	}
*/



	public static void main(String[] args) throws Throwable {

//		NumberRanges.test();

//		List list = new ArrayList();
//		doList(list);

//		PrimesSearchTest.test();


		String s = "ABC\uD835\uDD6b D";

		System.out.println(s.offsetByCodePoints(0, 4));

		if (true) return;


		long pid = ProcessHandle.current().pid();
		System.out.println("PID: " + pid);

		final TransferQueue<String> queue = new LinkedTransferQueue<>();

		Thread t1 = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);

					String value = queue.take();
					System.out.println(Thread.currentThread().getName() + " taken element: " + value);

					Thread.sleep(100);

					throw new RuntimeException("====");

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "Tread_1");

		t1.setUncaughtExceptionHandler((thread, thr) -> {
			thr.printStackTrace();
		});
		t1.start();

		queue.transfer("a");


		if (true) return;

		Properties properties = System.getProperties();
		properties.entrySet().stream()
				.sorted(Comparator.comparing(o2 -> o2.getKey().toString()))
				.forEach(entry-> System.out.println(entry.getKey() + " = " + entry.getValue()));

	}


	static class A {

		private final String data;

		A(String data) {
			this.data = data;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof A;
		}
	}

	static class B extends A {

		B(String data) {
			super(data);
		}
	}

	static class C extends B {

		C(String data) {
			super(data);
		}
	}

}
