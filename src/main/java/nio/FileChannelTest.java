package nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;

public class FileChannelTest {

	public static void main(String[] args) throws Exception {

		Pipe pipe = Pipe.open();

		Thread receiver = new Thread(() -> {
			ReadableByteChannel dst = pipe.source();
			ByteBuffer buffer = ByteBuffer.allocate(10);
			try {
				while (dst.read(buffer) >= 0) {
					buffer.flip();
					while (buffer.remaining() > 0) {
						System.out.println("SERVER Receive: " + (buffer.get() & 255));
					}
					buffer.clear();
				}
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
			}
		});
		receiver.start();

		ByteBuffer b = ByteBuffer.allocate(10);
		b.put((byte) 'A');
		b.flip();
		System.out.println(pipe.sink().write(b));


	}

	public static void test() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("temp.txt", "rw");
		FileChannel fc = raf.getChannel();
		long initPos = fc.position();
		System.out.printf("1. Position: %d; Size: %d\n", initPos, fc.size());

		String msg = "This is a test message";
		ByteBuffer buffer = ByteBuffer.allocateDirect(msg.length() * 2);
		buffer.asCharBuffer().put(msg);

		fc.write(buffer);
		fc.force(true);
		System.out.printf("2. Position: %d; Size: %d\n", fc.position(), fc.size());

		buffer.clear();
		fc.position(initPos);
		fc.read(buffer);
		buffer.flip();

		System.out.print("3. Buffer value: ");
		while (buffer.hasRemaining()){
			System.out.print(buffer.getChar());
		}

		System.out.println();
	}



	public static void testFileMap() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("temp.txt", "rw");
		FileChannel fc = raf.getChannel();

		MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
		buffer.put((byte) 'O');
		buffer.clear();

	}


	public static void testTransfer() {
		try (FileInputStream fis = new FileInputStream("temp.txt")){
			FileChannel inChannel = fis.getChannel();
			WritableByteChannel outChannel = Channels.newChannel(System.out);
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException ioe){
			System.out.println("I/O error: " + ioe.getMessage());
		}
	}

}
