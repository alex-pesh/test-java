package core;

import java.util.function.Function;

// https://stackoverflow.com/questions/335311/what-is-the-difference-between-a-static-and-a-non-static-initialization-code-blo

public class ClassInitTest {

	public static void main(String[] args) throws Exception {
		new ClassInitTest().testInner();
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

}
