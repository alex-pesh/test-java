package proxy.custom.fastproxy;

public class FastProxyTest {

	public static void main(String[] args) throws Throwable {

		interface Greeter {
			String greet(String name);
			String bye(String name, int i);
		}

		Greeter g = new FastProxy().create(Greeter.class, (proxy, method, a) -> {
			return switch (method.getName()) {
				case "greet" -> "Hello, " + a[0];
				case "bye"   -> "Bye, " + a[0] + "_" + a[1];
				default -> throw new UnsupportedOperationException();
			};
		});

		System.out.println(g.greet("Alice")); // Hello, Alice
		System.out.println(g.bye("Bob", 890));     // Bye, Bob
	}

}
