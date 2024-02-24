package patterns.bridge;

public class ProgramTest {

	public static void main(String[] args) {

		Program[] programs = new Program[] {
				new BankProgram("Bank Program", new JavaDeveloper()),
				new StockProgram("Stock Program", new CppDeveloper()),
		};

		for (Program program : programs) {
			program.develop();
		}
	}

}
