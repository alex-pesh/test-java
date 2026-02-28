package misc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class ObjectAddress {

	private static Unsafe unsafe;

	static {
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (Unsafe)field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static long addressOf(Object o) throws Exception {
		Object[] array = new Object[] {o};

		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		int addressSize = unsafe.addressSize();

		long objectAddress = switch (addressSize) {
			case 4 -> unsafe.getInt(array, baseOffset);
			case 8 -> unsafe.getLong(array, baseOffset);
			default -> throw new Error("unsupported address size: " + addressSize);
		};

		return(objectAddress);
	}


	public static void printAddresses_v2(String label, Object... objects) {

		System.out.print(label + ": 0x");
		long last = 0;
		int offset = unsafe.arrayBaseOffset(objects.getClass());
		int scale = unsafe.arrayIndexScale(objects.getClass());
		final boolean is64bit = true;

		switch (scale) {
			case 4:
				long factor = is64bit ? 8 : 1;
				final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
				System.out.print(Long.toHexString(i1));
				last = i1;
				for (int i = 1; i < objects.length; i++) {
					final long i2 = (unsafe.getInt(objects, offset + i * 4L) & 0xFFFFFFFFL) * factor;
					if (i2 > last)
						System.out.print(", +" + Long.toHexString(i2 - last));
					else
						System.out.print(", -" + Long.toHexString( last - i2));
					last = i2;
				}
				break;
			case 8:
				throw new AssertionError("Not supported");
		}
		System.out.println();
	}


	public static void main(String... args) throws Exception {
/*		Object mine = "Hello".toCharArray();
		long address = addressOf(mine);
		System.out.println("Addess: " + address);
		printBytes(address, 27);*/

		Runnable r = () -> {
			Object o = new Object();
			try {
				Thread.sleep(200);
				printAddresses_v2("Thread: " + Thread.currentThread().getName() + " ; Object address ", o);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		};

		Thread t1 = new Thread(r, "Thread_1");
		Thread t2 = new Thread(r, "Thread_2");

		r.run();
		r.run();

		t1.start();
		t2.start();

	}
}
