package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.util.concurrent.Callable;

public class SelectorTest {

	public static void test() throws IOException {

		Selector selector = Selector.open();
		SocketAddress address = new InetSocketAddress("translate.google.com", 80);
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		SelectionKey key = channel.register(selector, SelectionKey.OP_CONNECT, "test_channel");
		Thread thread = new Thread(new SelectorRoutine(selector), "SelectorThread");
		thread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		selector.wakeup();

		channel.connect(address);
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
					System.out.println("Before select");

					selectedCnt = selector.select();

					System.out.println("After select");
				} catch (IOException e) {
					throw new RuntimeException("", e);
				}

				if (selectedCnt > 0) {
					selector.selectedKeys().forEach(sk -> {
						System.out.print(sk.attachment());
						System.out.print(": ");
						System.out.println(sk.interestOps());

						try {
							((SocketChannel) sk.channel()).finishConnect();
						} catch (IOException e) {
							e.printStackTrace();
						}

					});
				}

			}

		}
	}

}
