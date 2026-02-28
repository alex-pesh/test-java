package benchmark;

import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
public class StringFormatBenchmark {
	private String name = "UserName";
	private String lName = "LUserName";
	private String nick = "UserNick";


	@SneakyThrows
	public static void main(String[] args) {
		org.openjdk.jmh.Main.main(args);
	}


	@Benchmark
	public void stringFormat(Blackhole blackhole) {
		final String result = String.format("Contact {name=%s, lastName=%s, nickName=%s}", name, lName, nick);
		blackhole.consume(result);
	}


	@Benchmark
	public void stringBuilder(Blackhole blackhole) {
		final StringBuffer sb = new StringBuffer("Contact {");
		sb.append(", name='").append(name)
				.append(", lastName='").append(lName)
				.append(", nickName='").append(nick)
				.append('}');
		final String result = sb.toString();
		blackhole.consume(result);
	}

}