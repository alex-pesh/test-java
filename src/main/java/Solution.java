import lombok.extern.slf4j.Slf4j;
import proxy.custom.fastproxy.MethodHandler;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.*;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Slf4j
public class Solution {

	public static void main(String[] args) throws Throwable {

		ExecutorService executor = Executors.newFixedThreadPool(10);

		IntStream.range(0, 10).forEach(i -> executor.submit(() -> {
			try {
				double d = divide();
				System.out.println(d);
			} catch (Exception e) {
				log.error("Failed to read data");
			}
		}));

		System.out.println();
	}

	public static double divide() throws IOException {
		String host = "ya.ru";
		SocketFactory socketFactory = SSLSocketFactory.getDefault();
		Socket socket = socketFactory.createSocket(host, 443);

		OutputStream out = socket.getOutputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String httpRequest = "GET / \r\n" +
				"Connection: close\r\n" + // Request the server close the connection after response
				"\r\n"; // Empty line signals the end of the request headers

		out.write(httpRequest.getBytes());
		out.flush();

		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}

//		int result = 1/0;
//		Scanner scanner = new Scanner(in);
//		while (scanner.hasNextInt()) {
//			result /= scanner.nextInt();
//		}
//		scanner.close();

		socket.close();

		return 0;
	}


	public void doThis(Consumer<String> consumer, Supplier<Integer> supplier) {
		consumer.accept("TEST");
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Test {
		String name();
	}

	public static class Foo implements Test {

		@Override
		public String name() {
			return "";
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return Test.class;
		}


	}

}

