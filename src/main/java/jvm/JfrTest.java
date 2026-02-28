package jvm;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import jdk.jfr.consumer.EventStream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class JfrTest {
	static int NUMBER_CPULOAD_EVENTS = 3;


	public static void main(String[] args) {
		testRemoteVMEvents("52671");
	}



	public static void testLocalEvents() throws IOException {

		AtomicInteger timer = new AtomicInteger();

		try (EventStream es = EventStream.openRepository()) {
			es.onEvent("jdk.CPULoad", event -> {
				System.out.println("CPU Load " + event.getEndTime());
				System.out.println(" Machine total: " + 100 * event.getFloat("machineTotal") + "%");
				System.out.println(" JVM User: " + 100 * event.getFloat("jvmUser") + "%");
				System.out.println(" JVM System: " + 100 * event.getFloat("jvmSystem") + "%");
				System.out.println();
				if (timer.incrementAndGet() == NUMBER_CPULOAD_EVENTS) {
					System.exit(0);
				}
			});
			es.start();
		}
	}


	/**
	 * Подключается к JFR-репозиторию удаленной запущенной JVM с PID=vmPid и читает из него события.
	 * Целевая JVM должна быть запущена с включенным JFR: <code>XX:StartFlightRecording:filename=path_to_file.jfr</code>
	 * или через <code>jcmd JVM_PID JFR.start</code>
	 *
	 * @param vmPid
	 */
	public static void testRemoteVMEvents(String vmPid) {

		String repositoryPath;
		try {
			VirtualMachine vm = VirtualMachine.attach(vmPid);
			repositoryPath = vm.getSystemProperties().getProperty("jdk.jfr.repository");
		} catch (Exception e) {
			throw new RuntimeException("Failed to attach vm", e);
		}

		try (EventStream es = EventStream.openRepository(Path.of(repositoryPath))) {
			es.onEvent(e -> {
				System.out.println(e);
			});
			es.startAsync();
		} catch (IOException e) {
			throw new RuntimeException("Failed to open jfr repository", e);

		}
	}


}
