package proxy.custom.fastproxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;


/**
 * Пример проксирования методов интерфейса через генерацию лямбд
 * <p>
 * LambdaMetafactory требует, чтобы сигнатура MethodHandle точно соответствовала сигнатуре абстрактного метода интерфейса
 */


public class FastProxy {

	@FunctionalInterface
	public interface Handler {
		Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
	}

	public <T> T create(Class<T> iface, Handler handler) {
		if (!iface.isInterface()) throw new IllegalArgumentException("Only interfaces supported");

		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();

			// MH базовый: Handler.invoke(Object proxy, Method method, Object[] args)
			MethodHandle base = lookup.findVirtual(Handler.class, "invoke", MethodType.methodType(Object.class, Object.class, Method.class, Object[].class)).bindTo(handler);
			// base: (Object proxy, Method method, Object[] args) -> Object  (but method is not bound yet)

			// Для каждого Method создаём MethodHandle, который имеет сигнатуру:
			// (Object proxy, param1, param2, ...) -> Object
			Map<Method, MethodHandle> mhMap = new HashMap<>();
			for (Method m : iface.getMethods()) {
				int paramCount = m.getParameterCount();

				// вставляем Method как второй аргумент (индекс 1)
				MethodHandle boundMethod = MethodHandles.insertArguments(base, 1, m);
				// boundMethod: (Object proxy, Object[] args) -> Object

				// собираем параметры в Object[]: превращаем (proxy, p1, p2, ...) -> Object в
				// (proxy, p1, p2, ...) -> Object, делая collector для последних paramCount аргументов
				MethodHandle collector = boundMethod.asCollector(Object[].class, paramCount);
				// collector: (Object proxy, p1, p2, ...) -> Object

				// Приведём тип к точной сигнатуре: (Object, paramTypes...) -> returnType
				MethodType targetType = MethodType.methodType(m.getReturnType(), Object.class, m.getParameterTypes());
				MethodHandle finalMH = collector.asType(targetType);

				mhMap.put(m, finalMH);
			}

			// Создаём InvocationHandler, который вызывает подготовленные MH
			InvocationHandler ih = (proxy, method, args) -> {
				MethodHandle mh = mhMap.get(method);
				if (mh == null) {
					// fallback: интерфейс может наследовать методы от Object
					if (method.getDeclaringClass() == Object.class) {
						return method.invoke(this, args);
					}
					throw new IllegalStateException("No MH for method: " + method);
				}

				// Формируем аргументы для invokeWithArguments: сначала proxy, потом параметры
				int paramCount = method.getParameterCount();
				Object[] callArgs;
				if (paramCount == 0) {
					callArgs = new Object[]{proxy};
				} else {
					callArgs = new Object[1 + paramCount];
					callArgs[0] = proxy;
					if (args != null) System.arraycopy(args, 0, callArgs, 1, paramCount);
				}

				try {
					return mh.invokeWithArguments(callArgs);
				} catch (Throwable t) {
					// unwrap invocation exceptions
					if (t instanceof InvocationTargetException)
						throw ((InvocationTargetException) t).getTargetException();
					throw t;
				}
			};

			// создаём и возвращаем Proxy
			Object proxy = Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, ih);
			return iface.cast(proxy);

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}


	private static Class<?> generateProxyClass(Class<?> iface) {

		DynamicType.Builder<?> builder =
				new ByteBuddy()
						.subclass(ProxyHolder.class)
						.implement(iface)
						.defineField("_handler", Handler.class, Visibility.PUBLIC);

		for (Method m : iface.getMethods()) {
			builder = builder
					.defineField(m.getName(), m.getDeclaringClass(), Visibility.PUBLIC)
					.method(ElementMatchers.is(m))
					.intercept(MethodDelegation.toField(m.getName()));
		}

		return builder.make()
				.load(FastProxy.class.getClassLoader())
				.getLoaded();
	}
}




/*

public final class FastProxy {

	@FunctionalInterface
	public interface Handler {
		Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
	}


	public <T> T create(Class<T> iface, Handler handler) {
		if (!iface.isInterface())
			throw new IllegalArgumentException("Only interfaces supported");

		try {
			Map<Method, MethodHandle> dispatchTable = new HashMap<>();

			MethodHandles.Lookup lookup = MethodHandles.lookup();

			for (Method m : iface.getMethods()) {

				// Handler.invoke(Object, Method, Object[])
				MethodHandle base = lookup.findVirtual(
						Handler.class,
						"invoke",
						MethodType.methodType(Object.class, Object.class, Method.class, Object[].class)
				);

				// bind Handler instance
				MethodHandle bound = base.bindTo(handler);

                */
/*
                    Нам нужно превратить MethodHandle, принимающий:

                        invoke(Object proxy, Method method, Object[] args)

                    в MethodHandle с сигнатурой:

                        (proxy, arg1, arg2, ..., argN) -> returnType
                *//*


				int paramCount = m.getParameterCount();

				// Сначала подставляем Method (фиксируем его)
				bound = bound.bindTo(m);

				// Затем собираем аргументы интерфейсного метода в Object[]
				bound = bound.asCollector(Object[].class, paramCount);

				// Приводим тип MH к типу оригинального метода интерфейса
				bound = bound.asType(MethodType.methodType(m.getReturnType(), Object.class, m.getParameterTypes()));

				dispatchTable.put(m, bound);
			}

			InvocationHandler ih = (proxy, method, args) -> {
				MethodHandle target = dispatchTable.get(method);

				if (target == null)
					throw new IllegalStateException("No MethodHandle for method " + method);

				// args может быть null для безаргументных методов — обрабатываем
				if (args == null) {
					return target.invoke(proxy);
				}

				return target.invokeWithArguments(prepareArgs(proxy, args));
			};

			// Создаем JDK Proxy (самое надежное)
			Object proxyInstance = Proxy.newProxyInstance(
					iface.getClassLoader(),
					new Class<?>[]{iface},
					ih
			);

			return iface.cast(proxyInstance);

		} catch (Throwable t) {
			throw new RuntimeException("Failed to create fast proxy", t);
		}
	}

	private static List<Object> prepareArgs(Object proxy, Object[] args) {
		List<Object> list = new ArrayList<>(args.length + 1);
		list.add(proxy);
		list.addAll(Arrays.asList(args));
		return list;
	}

	// ==== DEMO ====
	public static void main(String[] args) {

		interface Greeter {
			String greet(String name);
			String bye(String name);
		}

		Greeter g = FastProxy.create(Greeter.class, (proxy, method, a) -> {
			return switch (method.getName()) {
				case "greet" -> "Hello, " + a[0];
				case "bye"   -> "Bye, " + a[0];
				default -> throw new UnsupportedOperationException();
			};
		});

		System.out.println(g.greet("Alice")); // Hello, Alice
		System.out.println(g.bye("Bob"));     // Bye, Bob
	}
}
*/
