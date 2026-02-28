package core;

import java.util.function.BiFunction;

public class EnumsTest {


	public enum EnumFunctor {
		PLUS {
			public Number apply(int a, int b) {
				return a + b;
			}
		}, MINUS {
			public Number apply(int a, int b) {
				return a - b;
			}
		}, MULTIPLY {
			public Number apply(int a, int b) {
				return a * b;
			}
		}, DIVIDE {
			public Number apply(int a, int b) {
				return (double) a / b;
			}
		};

		public abstract Number apply(int a, int b);

	}


	public static void main(String[] args) {
		System.out.println(EnumFunctor.DIVIDE.apply(3, 10));
	}
}
