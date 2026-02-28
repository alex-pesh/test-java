package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChannelsTest {

	public static void main(String[] args) {
		testSocketChannel();
	}


	private static void testSocketChannel() {

		SocketAddress address = new InetSocketAddress("localhost", 8888);
		try (SocketChannel channel = SocketChannel.open(address)) {

			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.put("PING".getBytes(StandardCharsets.UTF_8));
//			buffer.putInt(5);
			buffer.flip();
			channel.write(buffer);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}


}
