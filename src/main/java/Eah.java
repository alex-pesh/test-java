import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Eah {

	int[] i;

	public static void main(String[] args) throws Exception {

		new A().data();

		if (true) {
			return;
		}

		ReferenceQueue<Object> queue = new ReferenceQueue<>();

		int[] ii = new int[]{};

		Object obj = new Object();
		Reference<Object> ref = new WeakReference<>(obj, queue);

		obj = null;
		System.gc();

		TimeUnit.SECONDS.sleep(2);

		Reference<?> removed = queue.poll();
		if (removed != null) {
			System.out.println("Ссылка была помещена в очередь.");
		} else {
			System.out.println("Ссылка не попала в очередь.");
		}
	}


	static class A {

		Number data() throws ReflectiveOperationException {
			return 1L;
		}

	}

	static class B extends A {
		@Override
		Integer data() throws IllegalAccessException {
			return 1;
		}
	}

}
