package patterns.chain;

public class ChainTest {


	public static void main(String[] args) throws Throwable {

		ChainHandler handler = new Handler_1();
		handler	.next(new Handler_2())
				.next(new Handler_3())
				.next(new Handler_4());

		handler.handle("XXCCC");

	}

	interface Handler {
		boolean handle(Object data);
		Handler next(Handler handler);
	}

	static class ChainHandler implements Handler {

		private Handler next;

		public Handler next(Handler handler) {
			this.next = handler;
			return handler;
		}

		protected boolean canHandle() {
			return true;
		}

		@Override
		public boolean handle(Object data) {
			if (canHandle()) {
				System.out.printf("Handling data '%s' by handler %s\n", data, this.getClass().getSimpleName());
				return true;
			}
			if (next != null) {
				return next.handle(data);
			}

			return false;
		}
	}



	static class Handler_1 extends ChainHandler {
		@Override
		public boolean handle(Object data) {
			return super.handle(data);
		}
	}

	static class Handler_2 extends ChainHandler {
		@Override
		public boolean handle(Object data) {
			return super.handle(data);
		}
	}

	static class Handler_3 extends ChainHandler {
		@Override
		public boolean handle(Object data) {
			System.out.printf("Rejected data '%s' by handler %s\n", data, this.getClass().getSimpleName());
			return false;
		}
	}

	static class Handler_4 extends ChainHandler { }

}

