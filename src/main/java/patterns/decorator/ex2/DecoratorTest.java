package patterns.decorator.ex2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class DecoratorTest {

	public static void main(String[] args) {

		DataSource dataSource = new CompressionDecorator(new EncryptionDecorator(new FileDataSource(Path.of("./tmp.txt"))));

		dataSource.write("Hello!".concat(System.lineSeparator()).getBytes(StandardCharsets.UTF_8));

		System.out.println(new String(dataSource.read(), StandardCharsets.UTF_8));

	}

}
