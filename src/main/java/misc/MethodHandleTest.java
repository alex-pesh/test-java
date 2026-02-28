package misc;

import javax.print.attribute.standard.PrinterResolution;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

/**
 *	https://blogs.oracle.com/javamagazine/post/java-reflection-method-handles
 * 	https://www.infoworld.com/article/3489879/better-than-reflection-using-method-handles-and-variable-handles-in-java.html
 * <p>
 * При вызове цепочек методов MethodHandle (bindTo, collectArguments, filterReturnValue и т.п)
 * JVM построит под это одну LambdaForm, которую потом JIT превратит в оптимизированный машинный код —
 * фактически, как если бы было явно определено Integer.toString(Math.abs(Integer.parseInt(s))).
 * <p>
 * VM делает примерно следующее:
 * <p>
 * {@snippet lang=TEXT:
 * LambdaForm:
 *   0: arg0 (String)
 *   1: result1 = invoke(parseInt, arg0)   // int
 *   2: result2 = invoke(abs, result1)     // int
 *   3: result3 = invoke(toString, result2) // String
 *   return result3
 * 	}
 * 	Если включить системное свойство {@code -Djava.lang.invoke.MethodHandle.DUMP_CLASS_FILES=true},
 * 	JVM будет сохранять в /tmp (или в user.dir) скомпилированные классы для всех LambdaForm —
 *  здесь можно увидеть байткод, который создаётся под капотом для твоего MethodHandle-цепочек.
 *  Каждый из этих классов можно декомпилировать с помощью javap -c для исследования.
 *  <p>
 *  Под капотом всё это строится из объектов LambdaForm и MemberName:
 *  <li> LambdaForm описывает какие операции выполнять.
 *	<li> JVM через MethodHandleNatives генерирует класс с методом invoke(...).
 *	<li> JIT потом оптимизирует вызовы до прямых машинных инструкций.
 */
public class MethodHandleTest {

	public static void main(String[] args) throws Throwable {
		test_filterReturnValue();
	}

	public static void test_methodhandle() {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			MethodHandle handle = lookup.findVirtual(String.class, "toUpperCase", MethodType.methodType(String.class));
			System.out.println(handle.invoke("hhhh"));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}


	/**
	 * Метод bindTo() — это способ частичного применения аргумента к MethodHandle.
	 */
	public static void test_bindTo() throws Throwable {
		MethodHandle mh = MethodHandles.lookup()
				.findVirtual(String.class, "substring", MethodType.methodType(String.class, int.class, int.class));

		String s = "HelloWorld";
		MethodHandle bound = mh.bindTo(s); // теперь первый аргумент (this) = "HelloWorld"

		String part = (String) bound.invoke(0, 5); // вызывает s.substring(0, 5)
		System.out.println(part); // Hello
	}


	/**
	 * collectArguments — позволяет подготовить аргументы одной функцией перед вызовом другой (похоже на compose())
	 */
	public static void test_collectArguments() throws Throwable {
		MethodHandle getLength = MethodHandles.lookup()
				.findVirtual(String.class, "length", MethodType.methodType(int.class));

		MethodHandle sqrt = MethodHandles.lookup()
				.findStatic(Math.class, "sqrt", MethodType.methodType(double.class, double.class));

		// collectArguments: сначала вызвать getLength(), потом sqrt()
		MethodHandle sqrtOfLength = MethodHandles.collectArguments(sqrt, 0, getLength);

		double result = (double) sqrtOfLength.invokeExact("Hello"); // sqrt(5.0)

		System.out.println(result);
	}


	/**
	 * filterReturnValue — постобработка результата — как andThen():
	 */
	public static void test_filterReturnValue() throws Throwable {
		MethodHandle parseInt = MethodHandles.lookup()
				.findStatic(Integer.class, "parseInt", MethodType.methodType(int.class, String.class));

		MethodHandle abs = MethodHandles.lookup()
				.findStatic(Math.class, "abs", MethodType.methodType(int.class, int.class));

		// filterReturnValue: применяем Math.abs к результату parseInt
		MethodHandle absParse = MethodHandles.filterReturnValue(parseInt, abs);

		int x = (int) absParse.invokeExact("-42"); // вернёт 42

		System.out.println(x);
	}


	public static void test_unreflect() {
		try {
//			Method method = Subject.class.getDeclaredMethod("getData");
//			System.out.println(method.invoke(new Subject()));

			Field getter = Subject.class.getDeclaredField("data");
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			MethodHandle handle = lookup.unreflectGetter(getter);
			System.out.println(handle.invoke(new Subject()));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}


	/**
	 * https://www.baeldung.com/java-variable-handles#1-guidelines-and-conventions
	 */
	public static void test_varhandle() {
		try {
			Subject subject = new Subject();
			final VarHandle handle = MethodHandles
					.privateLookupIn(Subject.class, MethodHandles.lookup())
					.findVarHandle(Subject.class, "data", String.class);

			handle.compareAndSet(subject, "DATA", "DATA_2");
			String value = (String) handle.get(subject);
			System.out.println(value);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}


	static class Subject {
		private String data = "DATA";

		public String getData() {
			return data;
		}
	}

}
