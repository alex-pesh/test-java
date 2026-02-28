package benchmark;


import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import proxy.DynamicProxyTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class DynamicProxyBenchmark {

	CharSequence target, proxy;

	@SneakyThrows
	public static void main(String[] args) {
		Options opt = new OptionsBuilder()
				.include(AdapterBenchmark.class.getSimpleName())
				.forks(1)
				.build();

		new Runner(opt).run();
	}



	@Setup
	public void setup() {
		target = "Proxy___Test";
		proxy = (CharSequence) Proxy.newProxyInstance(
				DynamicProxyTest.class.getClassLoader(), new Class[] {CharSequence.class},
				new DynamicProxyTest.Handler(target));
	}


	@Benchmark
	public void dynamicInvoke(Blackhole blackhole) {
		char c = proxy.charAt(10);
		blackhole.consume(c);
	}


	@Benchmark
	public void virtualInvoke(Blackhole blackhole) {
		char c = target.charAt(10);
		blackhole.consume(c);
	}



	public static class Handler implements InvocationHandler {

		private final Object target;

		public Handler(Object target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return method.invoke(target, args);
		}
	}

}
