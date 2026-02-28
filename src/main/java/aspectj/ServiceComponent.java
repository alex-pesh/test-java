package aspectj;

public class ServiceComponent {

	public static void main(String[] args) {
		new ServiceComponent().targetMethod(123);
	}

	public boolean targetMethod(int param) {
		return true;
	}

}
