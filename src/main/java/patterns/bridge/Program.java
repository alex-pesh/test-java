package patterns.bridge;

public abstract class Program {
	private final String name;
	protected final Developer developer;


	public Program(String name, Developer developer) {
		this.name = name;
		this.developer = developer;
	}

	public String getName() {
		return name;
	}

	public abstract void develop();

}
