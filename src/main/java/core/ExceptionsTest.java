package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ExceptionsTest {

	public static void main(String[] args) throws Exception {
		testExceptionalResource();
	}



	public static void testSuppressedException(String filePath) throws IOException {
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		} finally {
			fileIn.close();
		}
	}


	public static void testAddSuppressedException(String filePath) throws IOException {
		Throwable firstException = null;
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(filePath);
		} catch (IOException e) {
			firstException = e;
		} finally {
			try {
				fileIn.close();
			} catch (NullPointerException npe) {
				if (firstException != null) {
					npe.addSuppressed(firstException);
				}
				throw npe;
			}
		}
	}


	public static class ExceptionalResource implements AutoCloseable {

		public void processSomething() {
			throw new IllegalArgumentException("Thrown from processSomething()");
		}

		@Override
		public void close() throws Exception {
			throw new NullPointerException("Thrown from close()");
		}
	}


	public static void testExceptionalResource() throws Exception {
		try (ExceptionalResource exceptionalResource = new ExceptionalResource()) {
			exceptionalResource.processSomething();
		}
	}





	static class CustomException extends Exception {}

	public static void testCatch() throws MalformedURLException, ReflectiveOperationException {

		ClassLoader parentClassLoader = ExceptionsTest.class.getClassLoader().getParent();
		URL url = new File("/home/alexander/workspace/project/test-java/target/classes/core").toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(new URL[] {url}, parentClassLoader) {
			@Override
			protected Class<?> findClass(String name) throws ClassNotFoundException {
				return super.findClass(name);
			}
		};
		try {
			Class<?> exClass = classLoader.loadClass("core.ExceptionsTest$CustomException");
			CustomException exception = (CustomException) exClass.getDeclaredConstructor().newInstance();
			throw exception;
		} catch (CustomException | ClassNotFoundException e) {
			System.out.println("Exception caught: " + e);
		} finally {
			System.out.println("Finalization");
		}
	}




	/**
	 * Способ бросить checked исключение без объявления в сигнатуре метода, используя generics
	 */
	public static void test() {
		sneakyThrow(new Exception("CCCC"));
	}

	private static <E extends Throwable> void sneakyThrow(Throwable t) throws E {
		throw (E) t;
	}

}
