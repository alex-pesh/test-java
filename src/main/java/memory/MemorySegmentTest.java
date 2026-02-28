package memory;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

import static java.nio.file.StandardOpenOption.*;


public class MemorySegmentTest {
	public static void main(String[] args) throws Exception {
//		testArenaAlloc();
		testFileMapping();
	}


	/**
	 * Подключение клиента:
	 * <code>nc -U /home/alexander/workspace/segment.sock</code>
	 * <p>
	 * Здесь предлагается использовать либу для Vert.x Postgres Client:
	 * <br>
	 * https://www.morling.dev/blog/talking-to-postgres-through-java-16-unix-domain-socket-channels/
	 */
	public static void testFileMapping() {

		final Path FILE_PATH = Path.of("/home/alexander/workspace/segment.txt");
		final Path SOCKET_PATH = Path.of("/home/alexander/workspace/segment.sock");

		Set<OpenOption> opts = Set.of(CREATE, READ, WRITE);
		UnixDomainSocketAddress address = UnixDomainSocketAddress.of(SOCKET_PATH);

		try (FileChannel fc = FileChannel.open(FILE_PATH, opts);
			 ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
			 Arena arena = Arena.ofConfined()) {

			serverSocketChannel.bind(address);
			SocketChannel socketChannel = serverSocketChannel.accept();

//			socketChannel.connect(address);

			MemorySegment mapped = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1L << 8, arena);

/*
			mapped.set(ValueLayout.JAVA_CHAR, 0,  'H');
			mapped.set(ValueLayout.JAVA_CHAR, 2,  'E');
			mapped.set(ValueLayout.JAVA_CHAR, 4,  'L');
			mapped.set(ValueLayout.JAVA_CHAR, 6,  'L');
			mapped.set(ValueLayout.JAVA_CHAR, 8,  'O');
			mapped.set(ValueLayout.JAVA_CHAR, 10, '\n');
*/

			System.out.println("Mapped Address: " + mapped.address());
			System.out.println("Mapped Size: " + mapped.byteSize());
			System.out.println("Sending segment to socket: " + socketChannel.getLocalAddress());

			fc.transferTo(0, mapped.byteSize(), socketChannel);

		} catch (SocketException e) {
			throw new RuntimeException("Failed to connect socket", e);
		} catch (IOException e) {
			throw new RuntimeException("Failed to allocate segment", e);
		} finally {
			try {
				Files.delete(SOCKET_PATH);
			} catch (IOException _) {}
		}
	}


	public static void testArenaAlloc() throws Exception {

		Arena arena = Arena.ofShared();
//		Arena arena = Arena.ofConfined();

		MemorySegment segment = arena.allocate(ValueLayout.JAVA_INT);

		segment.set(ValueLayout.JAVA_BYTE, 0, (byte) 0);
		segment.set(ValueLayout.JAVA_BYTE, 1, (byte) 127);
		segment.set(ValueLayout.JAVA_BYTE, 2, (byte) 0);
		segment.set(ValueLayout.JAVA_BYTE, 3, (byte) 0);


		Thread t1 = new Thread(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			byte[] buf = segment.toArray(ValueLayout.JAVA_BYTE);

			System.out.println("Bytes: ");
			for (int i = 0; i < buf.length; i++) {
				System.out.printf("%d ", buf[i]);
			}
			System.out.println();

			System.out.println("Integer representation: ");
			System.out.println(segment.get(ValueLayout.JAVA_INT, 0));
		});

		t1.start();
		t1.join();

		arena.close();


	}

}
