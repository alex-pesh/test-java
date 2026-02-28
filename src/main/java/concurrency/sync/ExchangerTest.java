package concurrency.sync;

import java.util.concurrent.Exchanger;

public class ExchangerTest {

	public static void main(String[] args) {
		test();
	}


	public static void test() {

		Exchanger<String> stringExchanger = new Exchanger<>();

		Thread t1 = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(10000);
					String data = stringExchanger.exchange("Message from Thread 1");
					System.out.println("Thread 1: " + data);
				} catch (InterruptedException e) {
					throw new RuntimeException("", e);
				}
			}
		});

		Thread t2 = new Thread(() -> {
			while (true) {
				try {
					String data = stringExchanger.exchange("Message from Thread 2");
					System.out.println("Thread 2: " + data);
				} catch (InterruptedException e) {
					throw new RuntimeException("", e);
				}
			}
		});

		t2.start();
		t1.start();

	}

}
