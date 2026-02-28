package nio;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

public class ServerSocketTest {

	public static void main(String[] args) throws IOException {
		test();
	}


	/**
	 * echo HELLO | nc -U /var/tmp/test.socket
	 *
	 * @throws IOException
	 */
	public static void test() throws IOException {

		try (ServerSocketChannel channel = ServerSocketChannel.open(StandardProtocolFamily.UNIX)) {
			channel.configureBlocking(false);
			channel.bind(UnixDomainSocketAddress.of(Path.of("/var/tmp/test.socket")));

			Selector selector = Selector.open();
			channel.register(selector, SelectionKey.OP_ACCEPT);


			while (true) {
				int numReadyChannels = selector.select();
				if (numReadyChannels == 0) {
					continue;
				}

				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

				while (keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();

					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						if (client == null) {
							continue;
						}
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_READ);

					} else if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel();
						System.out.println("Reading from socket channel: " + client.getLocalAddress());

						ByteBuffer buffer = ByteBuffer.allocate(128);
						client.read(buffer);
						System.out.println("Received data: " + new String(buffer.array()));

						buffer.flip();
						client.write(buffer);

					} else if (key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						System.out.println("Reading from socket channel: " + client.getLocalAddress());
					}
				}
			}
		}

	}

}
