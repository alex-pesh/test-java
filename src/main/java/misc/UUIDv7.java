package misc;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.UUID;


public class UUIDv7 {

	public static void main(String[] args) {

		long start2 = System.nanoTime();
		var uuid2 = UUIDv7.randomUUID();
		long end2 = System.nanoTime();

		System.out.println(uuid2);
		System.out.println("Time: " + (end2 - start2));

		System.out.println();

	}


	private static final SecureRandom random = new SecureRandom();

	public static UUID randomUUID() {
		byte[] value = randomBytes();
		ByteBuffer buf = ByteBuffer.wrap(value);
		long high = buf.getLong();
		long low = buf.getLong();
		return new UUID(high, low);
	}

	public static byte[] randomBytes() {
		// random bytes
		byte[] value = new byte[16];
		random.nextBytes(value);

		long millis = System.currentTimeMillis();
		ByteBuffer timestamp = ByteBuffer.allocate(Long.BYTES);
		timestamp.putLong(millis);

//		byte[] timestamp = new byte[Long.BYTES];
//		for (int i = 0; i < Long.BYTES; i++) {w
//			timestamp[i] = (byte) (millis >> (Long.BYTES-i-1)*8 & 0xFF);
//		}

		System.arraycopy(timestamp.array(), 2, value, 0, 6);

		// version and variant
		value[6] = (byte) ((value[6] & 0x0F) | 0x70);
		value[8] = (byte) ((value[8] & 0x3F) | 0x80);

		return value;
	}

}