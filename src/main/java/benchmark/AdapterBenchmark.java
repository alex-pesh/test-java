package benchmark;

import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class AdapterBenchmark {

	@SneakyThrows
	public static void main(String[] args) {
		Options opt = new OptionsBuilder()
				.include(AdapterBenchmark.class.getSimpleName())
				.forks(1)
				.build();

		new Runner(opt).run();
	}


	private UserProto proto;
	private UserProtoAdapter adapter;

	@Setup(Level.Iteration)
	public void setup() {
		proto = new UserProto("Alice", 30);
		adapter = new UserProtoAdapter(proto);
	}

	@Benchmark
	public String directCall() {
		return proto.getName();
	}

	@Benchmark
	public String adapterCall() {
		return adapter.getName();
	}



	public static class UserProto {
		private final String name;
		private final int age;

		public UserProto(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() { return name; }
		public int getAge() { return age; }
	}


	public static class UserProtoAdapter implements UserDTO {
		private final UserProto proto;

		public UserProtoAdapter(UserProto proto) {
			this.proto = proto;
		}

		@Override
		public String getName() {
			return proto.getName();
		}

		@Override
		public int getAge() {
			return proto.getAge();
		}
	}

	public interface UserDTO {
		String getName();
		int getAge();
	}

}
