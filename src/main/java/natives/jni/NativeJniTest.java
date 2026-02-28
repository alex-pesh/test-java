package natives.jni;

public class NativeJniTest {

	public static void main(String[] args) {
		test();
	}

	/**
	 * 	env var has to be exported:
	 * 	<p>LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/path_to_lib_dir</p>
	 */
	public static void test() {
		System.loadLibrary("natives");
		String result = new NativeJniTest().nativeMethod(100, "Test Value");
		System.out.println(result);
	}


	public native String nativeMethod(int intArg, String stringArg);

}
