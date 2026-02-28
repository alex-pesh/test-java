import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main<T> {

	interface I {
		void print();
	}

	public I create() {
		return () -> System.out.println("Hello");
	}

	private I i = this::create;

	public void test() {
		i.print();
	}



	public static Unsafe getUnsafe() {
		try {
			return (Unsafe) getField(Unsafe.class, "theUnsafe").get(null);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}


	public static Field getField(Class<?> cls, String name) {
		Field f = findField(cls, name);
		if (f != null) f.setAccessible(true);
		return f;
	}


	public static Field findField(Class<?> cls, String name) {
		try {
			return cls.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}



	private static final Pattern ESCAPE_PATTERN = Pattern.compile("(?U)(^'|'$)|(?!'$|$)(\\s+)|(\\s+|'$|$)|('$|$)");

	public static String prepareTerm(String searchTerm) {
		if (searchTerm == null || searchTerm.isBlank()) {
			return "";
		}

		return ESCAPE_PATTERN.matcher(searchTerm).replaceAll(mr -> {
			if (mr.group(1) != null) return "";
			if (mr.group(2) != null) return ":*&";
			if (mr.group(3) != null) return ":*";
			return "";
		});

	}

	static class Person {}
	static class Employee extends Person {}
	static  class Manager extends Employee {}


	static int mod(int a, int b) {
		return a - b*(a/b);
	}


	static void foo(List<? super Employee> list) {
		list.add(new Manager());
	}

	public static void main(String[] args) throws Exception {


	}

}
