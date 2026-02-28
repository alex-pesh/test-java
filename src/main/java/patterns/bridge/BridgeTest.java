package patterns.bridge;

/**
 * Пример паттерна Bridge. Создается отдельная иерархия типов Program и Developer
 */
public class BridgeTest {

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
