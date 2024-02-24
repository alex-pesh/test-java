package patterns.bridge;

public class JavaDeveloper implements Developer {

	@Override
	public void writeCode(Program program) {
		System.out.println("Java developer writes code for " + program.getName());
	}

}
