package memory;

import java.nio.ByteBuffer;

public class ByteBufferTest {

	public static void main(String[] args) {
		test();
	}


	public static void test() {


		System.out.println("==== init ===");
		ByteBuffer buffer = ByteBuffer.allocate(20);


		System.out.println("==== write ===");
		for (int i = 0; i < buffer.capacity(); i++) {
			buffer.put((byte) i);
		}

		buffer.rewind();

		System.out.println("==== write ===");
		for (int i = 0; i < 10; i++) {
			buffer.put((byte) (i+100));
		}

/*
		System.out.println("==== flip ===");
		buffer.flip();
		printer.accept(buffer);
*/
/*
		System.out.println("==== clear ===");
		buffer.clear();
		printer.accept(buffer);
*/
/*
		System.out.println("==== compact ===");
		buffer.compact();
		printer.accept(buffer);
*/

		buffer.rewind();

		ByteBuffer buffer2 = buffer.duplicate();
		buffer2.put(new byte[] {1, 1});

		for (int i = 0; buffer.hasRemaining(); i++) {
			System.out.printf("%d ", buffer.get());
		}
	}
}
