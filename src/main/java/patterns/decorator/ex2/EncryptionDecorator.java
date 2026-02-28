package patterns.decorator.ex2;

public class EncryptionDecorator extends DataSourceDecorator {

	public EncryptionDecorator(DataSource target) {
		super(target);
	}

	@Override
	public void write(byte[] data) {
		System.out.println("Encrypting data before write");
		super.write(data);
	}

	@Override
	public byte[] read() {
		byte[] data = super.read();
		System.out.println("Decrypting data after read");
		return data;
	}
}
