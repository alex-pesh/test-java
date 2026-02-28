package net;

import java.net.*;

public class NetworkInterfaceTest {

	public static void main(String[] args) throws Exception {
		printLocalAddress();
	}


	public static void printLocalAddress() throws SocketException {
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10003);
			System.out.println(socket.getLocalAddress().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


	public static void printInfo() throws SocketException {
		System.out.println("Interfaces info:");

		NetworkInterface.networkInterfaces().forEach(ifc -> {
			try {
				System.out.println(ifc);
				System.out.printf("\tActive: %s\n", ifc.isUp());
				System.out.printf("\tVirtual: %s\n", ifc.isVirtual());
				System.out.println("\tAdresses:");
				ifc.inetAddresses().forEach(addr -> System.out.printf("\t\t %s\n", addr.getHostAddress()));
			} catch (SocketException e) {
				throw new RuntimeException(e);
			}

			System.out.println("-------------------");
		});
	}

}
