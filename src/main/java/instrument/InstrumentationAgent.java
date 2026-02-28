package instrument;

import java.lang.instrument.Instrumentation;

public class InstrumentationAgent {

	public static volatile Instrumentation inst;

	public static void premain(final String agentArgs, final Instrumentation inst) {
		InstrumentationAgent.inst = inst;
		System.out.println("InstrumentationAgent loaded: " + inst);

//		inst.addTransformer();
	}

	public static Instrumentation getInstrumentation() {
		return inst;
	}

}
