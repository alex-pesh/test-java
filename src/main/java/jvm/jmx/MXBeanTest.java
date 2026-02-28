package jvm.jmx;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MXBeanTest {

	public static void main(String[] args) {
		systemInfo();
	}


	public static void systemInfo() {

		com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)
				java.lang.management.ManagementFactory.getOperatingSystemMXBean();

		log.info("pid:{}", ProcessHandle.current().pid());
		log.info("availableProcessors:{}", Runtime.getRuntime().availableProcessors());
		log.info("TotalMemorySize, mb:{}", os.getTotalMemorySize() / 1024 / 1024);
		log.info("maxMemory, mb:{}", Runtime.getRuntime().maxMemory() / 1024 / 1024);
		log.info("freeMemory, mb:{}", Runtime.getRuntime().freeMemory() / 1024 / 1024);
	}

}
