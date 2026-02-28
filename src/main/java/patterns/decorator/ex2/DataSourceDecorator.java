package patterns.decorator.ex2;

public class DataSourceDecorator implements DataSource {

	private final DataSource target;

	public DataSourceDecorator(DataSource target) {
		this.target = target;
	}


	@Override
	public void write(byte[] data) {
		target.write(data);
	}

	@Override
	public byte[] read() {
		return target.read();
	}

}
