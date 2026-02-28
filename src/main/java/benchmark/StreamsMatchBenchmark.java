package benchmark;

import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;


/**
 * Stream оказался в ~2 раза медленнее.
 * <pre>
 * - Создание Stream (conditions.stream()) → объект и итератор
 * - Лямбда-переход для cond -> cond.test(item)
 * - VirtualCall на Predicate.test()
 * - Потеря эффекта “tight loop”, который JIT легче оптимизирует
 * - В итоге на коротком списке условий (3–5 элементов) накладные расходы на Stream существенно превышают полезную работу.
 * </pre>
 * Для обычного цикла почти минимальный overhead
 * <pre>
 * - Обычный цикл → tight loop
 * - JIT может полностью заинлайнить вызовы
 * - Нет дополнительных объектов, итераторов, лямбд
 * </pre>
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 15000, time = 50, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class StreamsMatchBenchmark {

	@SneakyThrows
	public static void main(String[] args) {
		Options opt = new OptionsBuilder()
				.include(StreamsMatchBenchmark.class.getSimpleName())
				.verbosity(VerboseMode.SILENT)
				.forks(1)
				.build();

		new Runner(opt).run();
	}


	@State(Scope.Thread)
	public static class TestState {
		List<Predicate<Integer>> conditions = List.of(
				i -> i > 0,
				i -> i < 1000,
				i -> i % 2 == 0,
				i -> i != 42
		);

		int value = 500;
	}


	@Benchmark
	public boolean streamMatch(TestState s) {
		return s.conditions.stream().allMatch(cond -> cond.test(s.value));
	}


	@Benchmark
	public boolean loopMatch(TestState s) {
		for (Predicate<Integer> cond : s.conditions) {
			if (!cond.test(s.value)) return false;
		}
		return true;
	}

}
