package concurrency;

public class DeadlockTest {

	public static void suspendLockTest() {

		Object lock = new Object();

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (lock) {
						try {
							System.out.println(Thread.currentThread().getName() + " lock");
							Thread.sleep(200);
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

					try {
						Thread.sleep(100);
						System.out.println(Thread.currentThread().getName() + " suspending " + t1.getName());
						t1.suspend();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}

					System.out.println(Thread.currentThread().getName() + " trying to lock");
					synchronized (lock) {
						try {
							System.out.println(Thread.currentThread().getName() + " lock");
							Thread.sleep(100);
							System.out.println(Thread.currentThread().getName() + " After wait");

							t1.resume();

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



	public static void mutualLockTest() {

		Object o1 = new Object();
		Object o2 = new Object();

		Runnable r = () -> {
			String threadName = Thread.currentThread().getName();

			while (true) {
				System.out.println(System.nanoTime() + " - " + threadName + " is trying to take o1");
				synchronized (o1) {
					try {
						System.out.println(System.nanoTime() + " - " + threadName + " has taken o1");
						o1.wait(100L);
						System.out.println(System.nanoTime() + " - " + threadName + " released o1");

						System.out.println(System.nanoTime() + " - " + threadName + " is trying to take o2");
						synchronized (o2) {
							System.out.println(System.nanoTime() + " - " + threadName + " has taken o2");
							o1.wait(100L);
							System.out.println(System.nanoTime() + " - " + threadName + " released o1");
						}
						System.out.println(System.nanoTime() + " - " + threadName + " released o2");

					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		};

		Thread t1 = new Thread(r, "Thread_1");
		Thread t2 = new Thread(r, "Thread_2");

		t1.start();
		t2.start();

	}


}
