package core;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;


/**
 * <a href="https://www.baeldung.com/java-variable-handles">https://www.baeldung.com/java-variable-handles</a>
 *
 * <p>VarHandle methods allow access to variables under specific memory ordering effects.</p>
 * <p>For most of the methods there are 4 memory ordering effects:</p>
 * <ul>
 * 	<li>Plain reads and writes guarantee bitwise atomicity for references and primitives under 32 bits.
 * 		Also, they impose no ordering constraints with respect to the other traits.</li>
 * 	<li>Opaque operations are bitwise atomic and coherently ordered with respect to access to the same variable.</li>
 * 	<li>Acquire and Release operations obey Opaque properties. Also, Acquire reads will be ordered only after matching Release mode writes.</li>
 * 	<li>Volatile operations are fully ordered with respect to each other.</li>
 * </ul>
 */
public class VarHandleTest {

	public static void main(String[] args) {
		testArray();
	}


	public static void testArray() {
		Holder holder = new Holder();
		double[] current = new double[]{0, 0},
					next = new double[]{0, 0};
		double delta = 0.5;

		do {
			next[0] = current[0] + delta;
			next[1] = current[1] + delta;
		} while (!HOLDER_VALUE.compareAndSet(holder, current, next));
	}


	private static VarHandle HOLDER_VALUE;
	static {
		try {
			HOLDER_VALUE = MethodHandles
					.privateLookupIn(Holder.class, MethodHandles.lookup())
					.findVarHandle(Holder.class, "value", double[].class);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public static class Holder {
		double[] value = new double[]{0, 0};
	}



	private static VarHandle C_VALUE;
/*
	static {
		try {
			VALUE = MethodHandles
					.privateLookupIn(C.class, MethodHandles.lookup())
					.findVarHandle(C.class, "value", String.class);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
*/

	public static void test() {

		try {
			C_VALUE = MethodHandles
					.privateLookupIn(C.class, MethodHandles.lookup())
					.findVarHandle(C.class, "value", String.class);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}


		C c = new C();
		C_VALUE.set(c, "AAAAA");
		System.out.println(c.value);
	}


	public static class C {
		private String value;
	}

}
