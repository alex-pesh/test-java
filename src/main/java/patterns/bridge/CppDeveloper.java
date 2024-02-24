package patterns.bridge;

public class CppDeveloper implements Developer {

	@Override
	public void writeCode(Program program) {
		System.out.println("Cpp developer writes code for " + program.getName());
	}

}
