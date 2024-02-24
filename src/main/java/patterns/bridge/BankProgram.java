package patterns.bridge;

public class BankProgram extends Program {

	public BankProgram(String name, Developer developer) {
		super(name, developer);
	}

	@Override
	public void develop() {
		System.out.println("Bank program is being developed");
		developer.writeCode(this);
	}

}
