package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class PipeTest {

	public static void main(String[] args) throws Exception {
		testPipe();
	}



	public static void testPipe() throws IOException {
		final int BUF_SIZE = 10;
		final int LIMIT = 3;
		final Pipe pipe = Pipe.open();

		Thread sender = new Thread(() -> {
			WritableByteChannel src = pipe.sink();
			ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);

			for (int i = 0; i < LIMIT; i++) {
				buffer.clear();
				System.out.print("CLIENT Send:    ");
				for (int j = 0; j < BUF_SIZE; j++) {
					double random = Math.random();
					buffer.put((byte) (random * 256));
					System.out.printf("%d ", (byte) (random * 256));
				}

				System.out.print("\n");

				try {
					buffer.flip();
					while (src.write(buffer) > 0) ;
				} catch (IOException ioe) {
					System.err.println(ioe.getMessage());
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			try {
				src.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		});


		Thread receiver = new Thread(() -> {
			ReadableByteChannel dst = pipe.source();
			ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
			try {
				while (dst.read(buffer) >= 0) {
					buffer.flip();
					System.out.print("SERVER Receive: ");
					while (buffer.remaining() > 0) {
						System.out.printf("%d ", buffer.get());
					}
					buffer.clear();
					System.out.print("\n\n");
				}
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
			}
		});

		sender.start();
		receiver.start();
	}

}
