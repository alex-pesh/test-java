package core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Ковариантность/контравариантность типов – это сохранение/обращение порядка наследования для производных типов.
 * Проще говоря, когда у ковариантных сущностей типами-параметрами являются родитель и наследник, они сами становятся
 * как бы родителем и наследником. Контравариантные наоборот, становятся наследником и родителем.
 *
 * Инвариантность – это отсутствие свойств ковариантности и контрвариантности.
 * Дженерики без вайлдкардов инвариантны: List<Number> нельзя положить ни в переменную типа List<Double>, ни в List<Object>.
 *
 * Массивы ковариантны: в переменную Object[] можно присвоить значение типа String[].
 *
 * Переопределение методов начиная с Java 5 ковариантно относительно типа результата и типов исключений.
 * В Java отсутствует ковариантность (https://t.me/BookJava/1451) переопределенных методов по параметрам – их типы должны
 * совпадать с типами параметров метода в родительском классе.

 *  Erasure is the technique of mapping richer types at one level to less rich types at a lower level.
 * https://cr.openjdk.org/%7Ebriangoetz/valhalla/erasure.html
 *
 *
 *
 * Restrictions on Generics:
 * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html
 */
public class GenericsTest {


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
	 *  cannot take List<Integer>, List<String>, List<Double>, and so on,
	 *  because they are not subtypes of List<Object> ( List<Object> is invariant)
	 *  To write a generic method, need to use Unbounded Wildcards: List<?>
	 */
	private static void doList(List<Object> list) {
		list.add(new B(""));
	}


	public static void test() {
//		doList(new ArrayList<Number>());
	}


	/**
	 * <a href="https://docs.oracle.com/javase/tutorial/java/generics/nonReifiableVarargsType.html">Non-Reifiable Types</a>
	 */
	public static void reifiableTest() {
		List<? extends Number>[] data1 = new List[2];
		List<? extends Object>[] data2 = new List<?>[2]; // List<? extends Object> is not reifiable. List<?> is reifiable
//		List<? extends Number>[] data3 = new List<?>[2]; // compilation error
//		List<? extends Object>[] data4 = new List<? extends Object>[2]; // compilation error
	}


	/**
	 * Bridge method
	 * В Java отсутствует ковариантность переопределенных методов по параметрам – их типы должны совпадать с типами
	 * параметров метода в родительском классе. Когда дженерик параметр конкретизируется в наследнике, методы с
	 * аргументами этого дженерик типа больше не совпадают в байткоде – в наследнике тип конкретный, а в родителе стертый до верхней границы.
	 * Проблема решается простым и безопасным кастом. Компилятор генерирует новый метод, который совпадает по сигнатуре
	 * с родительским. В его теле параметр кастуется и вызов делегируется в пользовательский метод. Это и называется bridge методом.
	 * Bridge method можно увидеть с помощью рефлекшна. Его имя совпадает с оригинальным методом, но параметр имеет тип,
	 * в который сотрется дженерик родителя. Этот метод будет помечен флагом synthetic, что значит, что он написан не программистом а компилятором.
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
			System.out.println(method);
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


//	class MathException<T> extends Exception { } // A generic class cannot extend the Throwable class directly or indirectly

}
