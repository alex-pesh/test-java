package patterns.decorator.ex2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileDataSource implements DataSource {

	private Path path;

	public FileDataSource(Path path) {
		this.path = path;
	}


	@Override
	public void write(byte[] data) {
		try {
			Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write", e);
		}
	}

	@Override
	public byte[] read() {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read", e);
		}
	}

}
