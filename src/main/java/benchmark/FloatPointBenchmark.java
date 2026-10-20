package benchmark;

import lombok.SneakyThrows;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;


@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 50, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 50, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(3)
public class FloatPointBenchmark {

	@SneakyThrows
	public static void main(String[] args) {
		Options opt = new OptionsBuilder()
				.include(FloatPointBenchmark.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}



	MathContext mathCtx;

	@Setup()
	public void setup() {
		mathCtx = new MathContext(3, RoundingMode.HALF_UP);
	}


	@Benchmark
	public void fractionCall(Blackhole blackhole) {

		Fraction result = new Fraction(1.2)
				.multiply(new Fraction(0.4))
				.divide(new Fraction(0.8))
				.multiply(new Fraction(0.6))
				.add(new Fraction(1.25))
				.divide(new Fraction(0.75));

		blackhole.consume(result);
	}


	@Benchmark
	public void bigFractionCall(Blackhole blackhole) {

		BigFraction result = new BigFraction(1.2)
				.multiply(new BigFraction(0.4))
				.divide(new BigFraction(0.8))
				.multiply(new BigFraction(0.6))
				.add(new BigFraction(1.25))
				.divide(new BigFraction(0.75));

		blackhole.consume(result);
	}


	@Benchmark
	public void bigDecimalCall(Blackhole blackhole) {

		BigDecimal result = new BigDecimal("1.2")
				.multiply(new BigDecimal("0.4"))
				.divide(new BigDecimal("0.8"), mathCtx)
				.multiply(new BigDecimal("0.6"))
				.add(new BigDecimal("1.25"))
				.add(new BigDecimal("0.75"));

		blackhole.consume(result);
	}

}