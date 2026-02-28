package core;

public class InterfacesTest {

	public static String STRING = "STRING_VALUE";


	public static void main(String[] args) {
		C c = new C() {};

		c.foo();
	}


	public static interface A {
		default void foo() {
			System.out.println("A.foo()");
		}
	}


	public static interface B {
		default void foo() {
			System.out.println("B.foo()");
		}
	}


	public static interface C extends A, B {
		@Override
		default void foo() {
			A.super.foo();
			B.super.foo();
			System.out.println("C.foo()");
		}
	}


}
