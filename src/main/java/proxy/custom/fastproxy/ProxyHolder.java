package proxy.custom.fastproxy;

public abstract class ProxyHolder {
	protected MethodHandler _handler;

	public Object invoke(String name, Object[] args) throws Throwable {
		return _handler.invoke(this, name, args);
	}
}
