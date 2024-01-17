package concurrency;

/**
 * https://jenkov.com/tutorials/java-concurrency/false-sharing.html
 * https://stackoverflow.com/questions/72674269/jdk-internal-vm-annotation-contended-didnt-work
 * https://docs.oracle.com/en/java/javase/11/migrate/index.html#GUID-3A71ECEF-5FC5-46FE-9BA9-88CBFCE828CB
 * https://en.wikipedia.org/wiki/Cache_coherence
 */
public class FalseSharingExample {

	public static class Counter {
//		@jdk.internal.vm.annotation.Contended
		public volatile long count1 = 0;
		public volatile long count2 = 0;
	}


	public static void test() {

		Counter counter1 = new Counter();
		Counter counter2 = counter1;

		long iterations = 500_000_000;

		Thread thread1 = new Thread(() -> {
			long startTime = System.currentTimeMillis();
			for(long i=0; i<iterations; i++) {
				counter1.count1++;
			}
			long endTime = System.currentTimeMillis();
			System.out.println("total time: " + (endTime - startTime));
		});
		Thread thread2 = new Thread(() -> {
			long startTime = System.currentTimeMillis();
			for(long i=0; i<iterations; i++) {
				counter2.count2++;
			}
			long endTime = System.currentTimeMillis();
			System.out.println("total time: " + (endTime - startTime));
		});

		thread1.start();
		thread2.start();
	}

}
