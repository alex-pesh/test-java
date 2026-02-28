package patterns.decorator.ex2;

public interface DataSource {

	void write(byte[] data);
	byte[] read();

}
