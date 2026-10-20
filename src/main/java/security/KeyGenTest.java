package security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGenTest {

	public static void main(String[] args) throws Exception {
		generateKeyPair();
	}


	public static void generateKeyPair() throws Exception {
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(512);
		KeyPair keyPair = gen.generateKeyPair();

		System.out.println(keyPair.getPublic());
		System.out.println();
		System.out.println(keyPair.getPrivate());
	}
}
