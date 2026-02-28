package proxy.custom.fastproxy;

@FunctionalInterface
public interface MethodHandler {
	Object invoke(Object proxy, String methodName, Object[] args) throws Throwable;
}
