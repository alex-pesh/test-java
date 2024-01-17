package instrument;

import java.lang.instrument.Instrumentation;

public class InstrumentationAgent {

	public static volatile Instrumentation inst;

	static void premain(final String agentArgs, final Instrumentation inst) {
		InstrumentationAgent.inst = inst;
	}

	public static Instrumentation getInstrumentation() {
		return inst;
	}

}
