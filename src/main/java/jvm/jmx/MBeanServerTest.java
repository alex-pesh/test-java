package jvm.jmx;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class MBeanServerTest {

	public static void main(String[] args) {
		test();
	}


	public static void test() {

		try {
			ObjectName objectName = new ObjectName("com.test:type=basic,name=MemoryAllocator");
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.registerMBean(new MemoryAllocator(), objectName);
		} catch (JMException e) {
			throw new RuntimeException("", e);
		}

	}


	public static interface MemoryAllocatorMBean {
		void allocate(int bytesCount);
	}

	public static class MemoryAllocator implements MemoryAllocatorMBean {

		public void allocate(int bytesCount) {
			boolean[] data = new boolean[bytesCount];
			try {
				System.out.println("Allocated " + data.length + " bytes by thread " + Thread.currentThread().getName());
				Thread.sleep(1000);
				System.out.println(data.length);
				data = null;
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
