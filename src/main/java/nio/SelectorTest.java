package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

public class SelectorTest {

	public static void main(String[] args) throws IOException {
		test();
	}



	public static void test() throws IOException {

		// echo Hello | nc -l -k -p 8888

		Selector selector = Selector.open();
		SocketAddress address = new InetSocketAddress("localhost", 8888);
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		SelectionKey key = channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ, "test_channel");

		Thread thread = new Thread(new SelectorRoutine(selector), "SelectorThread");
//		thread.setDaemon(true);
		thread.start();

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		selector.wakeup();

		channel.connect(address);

//		while (!channel.finishConnect()) {
//			System.out.println("waiting to finish connection");
//		}
	}


	public static class SelectorRoutine implements Runnable {
		private Selector selector;

		public SelectorRoutine(Selector selector) {
			this.selector = selector;
		}

		@Override
		public void run() {
			int selectedCnt = 0;

			while (true) {
				try {
					selectedCnt = selector.select();
				} catch (IOException e) {
					throw new RuntimeException("", e);
				}

				if (selectedCnt == 0) {
					continue;
				}

				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

				while (keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();

					System.out.printf("SelectionKey: '%s', Operations: %d\n", key.attachment(), key.interestOps());

					SocketChannel channel = (SocketChannel) key.channel();
					System.out.printf("Channel open: %s\n", channel.isOpen());

					if (key.isConnectable()) {
						try {
							channel.finishConnect();
							System.out.println("Connection was established with a remote server");
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}

					if (key.isReadable() && channel.isConnected()) {
						try {
							ByteBuffer buffer = ByteBuffer.allocate(1000000);
							channel.read(buffer);
							System.out.println(new String(buffer.array()));
						} catch (Exception e) {
//							throw new RuntimeException(e);
							e.printStackTrace();
						}
					}

					keyIterator.remove();
				}
			}
		}
	}

}
