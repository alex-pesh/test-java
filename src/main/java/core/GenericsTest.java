package core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ковариантность/контравариантность типов – это сохранение/обращение порядка наследования для производных типов.
 * Проще говоря, когда у ковариантных сущностей типами-параметрами являются родитель и наследник, они сами становятся
 * как бы родителем и наследником. Контравариантные наоборот, становятся наследником и родителем.
 * <p>
 * Инвариантность – это отсутствие свойств ковариантности и контрвариантности.
 * Дженерики без вайлдкардов инвариантны: List&lt;Number&gt; нельзя положить ни в переменную типа <code>List&lt;Double&gt;</code>, ни в List&lt;Object&gt;.
 * <p>
 * Массивы ковариантны: в переменную Object[] можно присвоить значение типа String[].
 * <p>
 * Переопределение методов начиная с Java 5 ковариантно относительно типа результата и типов исключений.
 * В Java отсутствует ковариантность переопределенных методов по параметрам – их типы должны
 * совпадать с типами параметров метода в родительском классе.
 * <p>
 *  Erasure is the technique of mapping richer types at one level to less rich types at a lower level.
 * <br>
 * https://cr.openjdk.org/%7Ebriangoetz/valhalla/erasure.html
 * <p>
 * Restrictions on Generics:
 * <br>
 * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html
 */
public class GenericsTest {

	public static void main(String[] args) {
		bridgeMethodTest();
	}



	public static void rawTypesTest() {
		Box<String> strBox = new Box<>();
		Box rawBox = strBox;
		rawBox.set(new Object[]{});
		String s = strBox.value; // будет брошен ClassCastException
	}


	private static class Box<T> {
		private T value;
		public void set(T value) {
			this.value = value;
		}
	}



	/**
	 *  Массивы сами по себе ковариантны, но для обеспечения контроля типов
	 *  JVM бросит исключение ArrayStoreException в рантайме
	 */
	public static void arrayCovariance() {
		Number[] nums = new Integer[5];
		nums[0] = Integer.valueOf(1);
		nums[1] = Long.valueOf(1);
/*
		Object[] stringLists = new List<String>[2];  // compiler error, but pretend it's allowed
		stringLists[0] = new ArrayList<String>();   // OK
		stringLists[1] = new ArrayList<Integer>();  // An ArrayStoreException should be thrown, but the runtime can't detect it.
*/
	}


	/**
	 *  Cannot take <code>List&lt;Integer&gt;</code>, <code>List&lt;String&gt;</code>, <code>List&lt;Double&gt;</code>, and so on,
	 *  because they are not subtypes of <code>List&lt;Object&gt;</code> (<code>List&lt;Object&gt;</code> is invariant)
	 *  <br>To write a generic method, need to use Unbounded Wildcards: <code>List&lt;?&gt;</code>
	 */
	private static void doList(List<Object> list) {
		list.add(new B(""));
	}

	public static void test() {
//		doList(new ArrayList<Number>()); // Compilation error
	}


	/**
	 * A reifiable type is a type whose type information is fully available at runtime. This includes primitives,
	 * non-generic types, raw types, and invocations of unbound wildcards.
	 * <br>
	 * Non-reifiable types are types where information has been removed at compile-time by type erasure — invocations
	 * of generic types that are not defined as unbounded wildcards
	 * <p>
	 * <a href="https://docs.oracle.com/javase/tutorial/java/generics/nonReifiableVarargsType.html">Non-Reifiable Types</a>
	 */
	public static void reifiableTest() {
		List<? extends Number>[] data1 = new List[2];
		List<? extends Object>[] data2 = new List<?>[2]; // List<? extends Object> is not reifiable. List<?> is reifiable
//		List<? extends Number>[] data3 = new List<?>[2]; // compilation error
//		List<? extends Object>[] data4 = new List<? extends Object>[2]; // compilation error
	}


	/**
	 *
	 * <a href="https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html">Restrictions on Generics</a>
	 */
	public static <E> void nonReifiableTest(List<E> list1, List<?> list2) {
//		 compilation error: non-reifiable types cannot be used in an array
//		List<Number>[] data1 = new List<Number>[2];

//		 compile-time error: Because the Java compiler erases all type parameters in generic code,
//		 you cannot verify which parameterized type for a generic type is being used at runtime
//		if (list1 instanceof ArrayList<Integer>) {}

		if (list2 instanceof ArrayList<?>) {} // OK
	}


	/**
	 * <h4>Bridge method</h4>
	 * В Java отсутствует ковариантность переопределенных методов по параметрам – их типы должны совпадать с типами
	 * параметров метода в родительском классе. Когда generic параметр конкретизируется в наследнике, методы с
	 * аргументами этого generic-типа больше не совпадают в байткоде – в наследнике тип конкретный, а в родителе стертый до верхней границы.
	 * Проблема решается простым и безопасным кастом. Компилятор генерирует новый метод, который совпадает по сигнатуре
	 * с родительским. В его теле параметр кастуется и вызов делегируется в пользовательский метод. Это и называется bridge методом.
	 * Bridge method можно увидеть с помощью reflection. Его имя совпадает с оригинальным методом, но параметр имеет тип,
	 * в который сотрется generic родителя. Этот метод будет помечен флагом synthetic, что значит, что он написан не программистом, а компилятором.
	 * Попытка написать такой же метод вручную приведет к ошибке компиляции.
	 */
	public static void bridgeMethodTest() {

		Method[] methods = SuperGeneric.class.getDeclaredMethods();
		System.out.println("Declared methods of " + SuperGeneric.class.getSimpleName() + ':');
		for (Method method : methods) {
			System.out.println(method);
		}
		methods = ChildGeneric.class.getDeclaredMethods();
		System.out.println("\nDeclared methods of " + ChildGeneric.class.getSimpleName() + ':');
		for (Method method : methods) {
			System.out.println(method + " - synthetic method: " + method.isSynthetic());
		}

	}

	static class SuperGeneric<T extends Number> {
		public void setValue(T value) {	}
	}

	static class ChildGeneric extends SuperGeneric<Long> {
		@Override
		public void setValue(Long value) {
			super.setValue(value);
		}
	}


	/**
	 * Heap pollution состояние, когда параметризованной переменной присваивается непараметризованная,
	 * что в рантайме может привести к ClassCastException
	 *
	 * @return
	 */
	static List<String> makeHeapPollution() {
		List numbers = new ArrayList<Number>();
		numbers.add(1);
		List<String> strings = numbers;
		strings.add("");

		System.out.println(strings.get(0));

		return strings;
	}


	static void makeHeapPollution(List<String>... stringsLists) {
		Object[] array = stringsLists;
		List<Integer> numbersList = Arrays.asList(66,22,44,12);

		array[0] = numbersList;
		String str = stringsLists[0].get(0);
	}

// ===================================================================


	static class A {

		private final String data;

		A(String data) {
			this.data = data;
		}
		@Override
		public boolean equals(Object obj) {
			return obj instanceof A;
		}
	}

	static class B extends A {

		B(String data) {
			super(data);
		}
	}

	static class C extends B {

		C(String data) {
			super(data);
		}
	}

	static class Delegate<T extends A> {
		private T delegator;

		public <O extends A> void consume(O input) {}

		public T supply() {
			return delegator;
		}
	}


	static void supply(List<? extends A> collection) {
		A a = collection.getFirst();
	}


	static void collect(List<? super B> collection) {
		collection.add(new C(""));
	}





//	class MathException<T> extends Exception { } // A generic class cannot extend the Throwable class directly or indirectly

}
