package concurrency;

public class SuspendDeadlock {

	public static void test() {

		Object lock = new Object();

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (lock) {
						try {
							System.out.println(Thread.currentThread().getName() + " lock");
//							lock.wait(1000);
							Thread.sleep(100);
							System.out.println(Thread.currentThread().getName() + " After wait");
						} catch (InterruptedException e) {
							throw new RuntimeException("", e);
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "Thread___1");


		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (lock) {
						try {
							System.out.println(Thread.currentThread().getName() + " lock");
//							lock.wait(1000);
							Thread.sleep(100);
							System.out.println(Thread.currentThread().getName() + " After wait");

							t1.suspend();

						} catch (InterruptedException e) {
							throw new RuntimeException("", e);
						}
					}

					try {
						Thread.sleep(120);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "Thread___2");


		t1.start();
		t2.start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}



}
