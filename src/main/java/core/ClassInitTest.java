package core;

import java.util.function.Function;

// https://stackoverflow.com/questions/335311/what-is-the-difference-between-a-static-and-a-non-static-initialization-code-blo

/**
 * The code block with the static modifier signifies a class initializer; without the static modifier the code block is
 * an instance initializer.
 * <p>
 * Class initializers are executed in the order they are defined (top down, just like simple variable initializers) when
 * the class is loaded (actually, when it's resolved, but that's a technicality).
 * <p>
 * Instance initializers are executed in the order defined when the class is instantiated, immediately before the
 * constructor code is executed, immediately after the invocation of the super constructor.
 */
public class ClassInitTest {

	public static void main(String[] args) throws Exception {
//		testConstants();
		testConstructorOverride();
	}



	// Статический блок вызывается единожды на этапе загрузки класса
	static {
		System.out.println("========= ClassInitTest static block ===========");
	}


	// Initializer Block вызывается перед каждым вызовом конструктора. Бывает полезен в случаях, когда необходимо
	// выполнение кода при инициализации переменных или обработка ошибок. Этот код будет скопирован компилятором
	// в каждый конструктор.
	// Так же этот блок может быть полезен в анонимных внутренних классах, гже невозможно объявить конструктор
	{
		System.out.println("========= ClassInitTest init block ===========");
		this.data = new String("String data");
	}


	public ClassInitTest() {
		System.out.println("========= ClassInitTest constructor ===========");
		this.data = new String("String data");
	}


	private Object data = Integer.valueOf(911);
	private final Object finalData = new int[] {2};



	public static void testConstructor() {
		try {
			System.out.println("Loading class");
			Class c = ClassInitTest.class.getClassLoader().loadClass("core.ClassInitTest");
			System.out.println("Constructing class");
			c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Пример неверной инициализации не-final поля при обращении к нему из переопределяемого метода, вызванного из конструктора.
	 * При вызове конструктора Sub вызывается конструктор Super, в котором происходит вызов doSomething().
	 * При этом фактически вызывается метод doSomething, переопределенный в Sub, т.к. в vtable Super уже подставлен указатель на него.
	 * Но на этот момент конструктор Sub ещё не завершился и переменная value не проинициализировалась,
	 * хотя память под объект выделена и переменные получили значения по умолчанию.
	 * <p>
	 * finalValue — это constant variable (константная переменная по JLS): final-поле примитивного типа,
	 * инициализированное константным выражением 42. Компилятор Java подставляет значение такой константы прямо в места использования.
	 * То есть в doSomething() обращение к finalValue не является чтением поля объекта — оно скомпилировано как литерал
	 * и подставлено на место вызова (println).
	 * Само поле finalValue в этот момент в объекте всё ещё равно 0. Но этого не видно, потому что println читает не поле,
	 * а встроенную константу. Если посмотреть в отладчике на поле объекта в этот момент, там будет 0.
	 */
	static class Super {
		Super() {
			doSomething(); // переопределяемый метод
		}
		protected void doSomething() {}
	}

	static class Sub extends Super {
		private int intValue = 42;
		private final int finalIntValue = 42;
		private final int finalIntValueUnboxed = Integer.parseInt("42");

		private String strValue = "string value";
		private final String finalStrValue = "final string value";
		private final String finalStrObj = new String("final string obj");

		@Override
		protected void doSomething() {
			System.out.println("finalValue = " + finalIntValue);
			System.out.println("finalValueUnboxed = " + finalIntValueUnboxed); 	// напечатает 0, не 42
			System.out.println("value = " + intValue); 							// напечатает 0, не 42

			System.out.println("strValue = " + strValue);			// напечатает null, т.к. на момент вызова проинициализирован только значением по умолчанию
			System.out.println("finalStrValue = " + finalStrValue);	// напечатает "final string value", т.к. заинлайнил компилятор. Само поле будет null
			System.out.println("finalStrObj = " + finalStrObj);		// напечатает null, т.к. на момент вызова проинициализирован только значением по умолчанию,
																	// а inline не произойдет для объекта
		}
	}

	public static void testConstructorOverride() {
		new Sub();
	}




	public void testInner() {
		var f = new Function<Object, Object>() {
			{
				System.out.println("========= Function init block ===========");
			}

			@Override
			public Object apply(Object o) {
				System.out.println("========= Function call from " + ClassInitTest.this);
				return o;
			}
		};

		f.apply("DATA");
	}


	public static void testAnonymous() {
		var f = new Function<Object, Object>() {
			{
				System.out.println("========= Function init block ===========");
			}

			static {
				System.out.println("========= Function static block ===========");
			}

			@Override
			public Object apply(Object o) {
				return o;
			}
		};
	}


	/**
	 * При обращении к статическому полю {@code A}, которое является constant variable, класс Constants не будет
	 * инициализирован, т.к. вместо этого компилятор произведет вызов push этой константы на стек в месте обращения к полю
	 */
	static class Constants {
		static {
			System.out.println("Constants initialized");
		}

		static final int A = 10;
	}

	public static void testConstants() {
		System.out.println(Constants.A);
	}

}
