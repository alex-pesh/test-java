package net;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketImpl;

/**
 * Пример установки TCP_KEEPIDLE, TCP_KEEPINTVL, TCP_KEEPCNT в Java через JNA.
 * Работает только на Linux.
 */
public class TcpKeepalive {

	public static void main(String[] args) throws Exception {
//		Socket socket = new Socket("localhost", 8888);
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("localhost", 8888));

		// keepalive: idle=30s, interval=5s, count=3
		tune(socket, 2, 1, 3);
//		socket.setSoTimeout(1000);

		// Проверяем
		socket.getOutputStream().write("GET / HTTP/1.0\r\n\r\n".getBytes());
		socket.getOutputStream().flush();


		while (!socket.isClosed()) {
			Thread.sleep(1000);
		}
		socket.close();
	}


	// JNA-интерфейс к libc
	public interface CLibrary extends Library {
		CLibrary INSTANCE = Native.load("c", CLibrary.class);

		int setsockopt(int sockfd, int level, int optname, IntByReference optval, int optlen);
	}

	// Константы из <netinet/tcp.h> и <sys/socket.h>
	private static final int SOL_SOCKET     = 1;
	private static final int IPPROTO_TCP    = 6;
	private static final int SO_KEEPALIVE   = 9;
	private static final int TCP_KEEPIDLE   = 4;
	private static final int TCP_KEEPINTVL  = 5;
	private static final int TCP_KEEPCNT    = 6;

	public static void tune(Socket socket, int idleSec, int intvlSec, int cnt) throws Exception {
		// Извлечь file descriptor (fd) сокета
		int fd = getSocketFd(socket);

		setOption(fd, SOL_SOCKET, SO_KEEPALIVE, 1);

		setOption(fd, IPPROTO_TCP, TCP_KEEPIDLE, idleSec);
		setOption(fd, IPPROTO_TCP, TCP_KEEPINTVL, intvlSec);
		setOption(fd, IPPROTO_TCP, TCP_KEEPCNT, cnt);

		System.out.printf("TCP keepalive set: idle=%ds, interval=%ds, count=%d%n", idleSec, intvlSec, cnt);
	}

	private static void setOption(int fd, int level, int opt, int value) {
		IntByReference val = new IntByReference(value);
		int rc = CLibrary.INSTANCE.setsockopt(fd, level, opt, val, 4);
		if (rc != 0) {
			throw new IllegalStateException("setsockopt failed: errno=" + Native.getLastError());
		}
	}

	private static int getSocketFd(Socket socket) throws Exception {
		Field implField = Socket.class.getDeclaredField("impl");
		implField.setAccessible(true);
		SocketImpl impl = (SocketImpl) implField.get(socket);

		Method fdMethod = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
		fdMethod.setAccessible(true);
		Object fdObj = fdMethod.invoke(impl);

		Field fdIntField = fdObj.getClass().getDeclaredField("fd");
		fdIntField.setAccessible(true);
		return (Integer) fdIntField.get(fdObj);
	}

}
