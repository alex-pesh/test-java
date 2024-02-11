package proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyTest {
	private static final Logger logger = LoggerFactory.getLogger(DynamicProxyTest.class);


	public static void test() {

		CharSequence target = "Proxy___Test";

		CharSequence charSeqProxy = (CharSequence) Proxy.newProxyInstance(
				DynamicProxyTest.class.getClassLoader(), new Class[] {CharSequence.class},
				new Handler(target));

//		charSeqProxy.length();
		charSeqProxy.charAt(10);

		long startTime = System.nanoTime();
		target.charAt(10);
		long elapsedTime = System.nanoTime() - startTime;

		logger.info("Original elapsed time: {} ns", elapsedTime);

	}


	public static class Handler implements InvocationHandler {

		private final Object target;

		public Handler(Object target) {
			this.target = target;
		}


		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			logger.info("Before method invoking: {}", method.getName());
			long startTime = System.nanoTime();

			Object result = method.invoke(target, args);

			long elapsedTime = System.nanoTime() - startTime;

			logger.info("After method invoking: {}", method.getName());
			logger.info("Elapsed time: {} ns", elapsedTime);

			return result;
		}
	}
}
