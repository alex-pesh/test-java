package patterns.decorator.ex2;

public class CompressionDecorator extends DataSourceDecorator {

	public CompressionDecorator(DataSource target) {
		super(target);
	}

	@Override
	public void write(byte[] data) {
		System.out.println("Compressing data before write");
		super.write(data);
	}

	@Override
	public byte[] read() {
		byte[] data = super.read();
		System.out.println("Uncompressing data after read");
		return data;
	}

}
