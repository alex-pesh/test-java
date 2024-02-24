package patterns.bridge;

public class StockProgram extends Program {

	public StockProgram(String name, Developer developer) {
		super(name, developer);
	}

	@Override
	public void develop() {
		System.out.println("Stock program is being developed");
		developer.writeCode(this);
	}

}
