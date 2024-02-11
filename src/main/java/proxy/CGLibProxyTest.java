package proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CGLibProxyTest {
	private static final Logger logger = LoggerFactory.getLogger(CGLibProxyTest.class);

	public static void test() {

		Enhancer enhancer = new Enhancer();
//		enhancer.setSuperclass(CharSequence.class);
		enhancer.setInterfaces(new Class[] {CharSequence.class});
		enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {

			logger.info("Before method invoking: {}", method.getName());
			long startTime = System.nanoTime();

			Object result = proxy.invokeSuper(obj, args);

			long elapsedTime = System.nanoTime() - startTime;

			logger.info("After method invoking: {}", method.getName());
			logger.info("Elapsed time: {} ns", elapsedTime);

			return result;
		});

//		Service proxy = (Service) enhancer.create();
//		proxy.methodString("xxx");

		CharSequence charSequence = (CharSequence) enhancer.create();
		charSequence.charAt(5);
	}


	public static class Service {

		public String methodString(String input) {
			return input + "__processed";
		}

		public int methodInt(String input) {
			return input.length();
		}
	}
}
