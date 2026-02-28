package benchmark;

import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;


/**
 * https://habr.com/ru/companies/sberbank/articles/814299/
 * https://www.baeldung.com/java-microbenchmark-harness
 */

@State(Scope.Thread)
public class StreamsBenchmark {

	LongAdder adder = new LongAdder();
	List<Integer> list = new ArrayList<>();


	@SneakyThrows
	public static void main(String[] args) {
		org.openjdk.jmh.Main.main(args);
	}


	@Setup
	public void setup() {

		adder = new LongAdder();

		for (int i = 0; i < 100; i++) {
			list.add(i);
		}

	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	@Fork(value = 2, warmups = 1)
	@Threads(1)
	public void calculateSum(Blackhole blackhole) {

		int result = 0;
		for (Integer integer : list) {
			result += integer;
		}

/*
		list.forEach(adder::add);
		long result = adder.intValue();
*/

/*
		int[] result = new int[1];
		list.forEach(i -> result[0] =+ i);
*/

//		System.out.println("=== Result: "  +result);
//		int result = list.stream().reduce(0, Integer::sum);

//		int result = list.stream().collect(Collectors.summingInt(Integer::intValue));

//		int result = list.stream().mapToInt(Integer::intValue).sum();


		blackhole.consume(result);
	}

}