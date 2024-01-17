package misc;

import javax.print.attribute.standard.PrinterResolution;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleTest {

	public static void test() {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			MethodHandle handle = lookup.findStatic(PrinterResolution.class, "isValid", MethodType.methodType(boolean.class, String.class));
			System.out.println(handle.invoke("hhhh"));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
