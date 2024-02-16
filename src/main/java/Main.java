import concurrency.SuspendDeadlock;
import core.GenericsTest;
import natives.NativeTest;

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



	public static void main(String[] args) throws Throwable {

//		NumberRanges.test();

//		List list = new ArrayList();
//		doList(list);

//		PrimesSearchTest.test();

		NativeTest.test();

//		GenericsTest.bridgeMethodTest();

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




}
