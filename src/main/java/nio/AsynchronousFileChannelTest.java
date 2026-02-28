package nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class AsynchronousFileChannelTest {

	public static void main(String[] args) throws IOException {
		test();
	}


	private static void test() throws IOException {
		Path path = Path.of("/home/alexander/ntfs/data/APIConnection.zip");
		ExecutorService executor = Executors.newSingleThreadExecutor();
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, Set.of(StandardOpenOption.READ), executor);
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		channel.read(buffer, 0, null, new Handler());

		log.info("After read");
	}


	private static class Handler implements CompletionHandler<Integer, Void> {

		@Override
		public void completed(Integer result, Void attachment) {
			log.info("Bytes read: {}", result);
		}

		@Override
		public void failed(Throwable exc, Void attachment) {
			log.warn("Failed to read file", exc);
		}
	}

}
