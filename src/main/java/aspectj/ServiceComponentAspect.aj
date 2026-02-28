package aspectj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://www.baeldung.com/aspectj
 */
public aspect ServiceComponentAspect {

	private static final Logger logger = LoggerFactory.getLogger(ServiceComponentAspect.class);

	pointcut callTarget(int param, ServiceComponent component):
			call(boolean aspectj.ServiceComponent.targetMethod(int)) && args(param) && target(component);

	before(int param, ServiceComponent component) : callTarget(param, component) {
		logger.info("Before invoking of method targetMethod with param: {}", param);
	}

	boolean around(int param, ServiceComponent component) : callTarget(param, component) {
		return proceed(param, component);
	}

	after(int param, ServiceComponent component) : callTarget(param, component) {
		logger.info("After invoking of method targetMethod with param: {}", param);
	}
}
