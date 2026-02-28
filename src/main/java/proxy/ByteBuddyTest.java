package proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyTest {


	public static void main(String[] args) throws Exception {

		Class<? extends Greeter> proxyClass = new ByteBuddy()
				.subclass(Greeter.class)
				.method(ElementMatchers.named("greet"))
				.intercept(MethodDelegation.to(new GreeterInterceptor())) // <- объект делегата
				.make()
				.load(ByteBuddyTest.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();

		Greeter proxy = proxyClass.getDeclaredConstructor().newInstance();
		System.out.println(proxy.greet("Alice"));
	}



	public static class Greeter {
		public String greet(String name) {
			return "Hello, " + name;
		}
	}


	public static class GreeterInterceptor {
		// Метод должен быть public
		public String greet(String name) {
			return "Intercepted: Hello, " + name;
		}
	}

}
