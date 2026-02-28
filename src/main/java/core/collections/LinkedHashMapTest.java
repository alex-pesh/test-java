package core.collections;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class LinkedHashMapTest {

	public static void main(String[] args) {
		testLRUCache();
	}


	public static void testLRUCache() {
		LinkedHashMap<Integer, Integer> map = new LimitedLinkedHashMap<>(5);
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);
		map.put(5, 5);

		System.out.println(map.keySet());

//		map.get(1);
//		map.get(2);

		map.put(6, 6);
		map.put(7, 7);

		System.out.println(map.keySet());
	}


	@Test
	public void accessOrderTest() {
		LinkedHashMap<Integer, String> map = new LinkedHashMap<>(5);
		map.put(1, null);
		map.put(2, null);
		map.put(3, null);
		map.put(4, null);
		map.put(5, null);

		Set<Integer> keys = map.keySet();
		assertEquals("[1, 2, 3, 4, 5]", keys.toString());

		map.get(4);
		assertEquals("[1, 2, 3, 5, 4]", keys.toString());

		map.get(1);
		assertEquals("[2, 3, 5, 4, 1]", keys.toString());

		map.get(3);
		assertEquals("[2, 5, 4, 1, 3]", keys.toString());
	}


	@Test
	public void removeEldestEntryTest() {
		LinkedHashMap<Integer, String> map = new LimitedLinkedHashMap<>(5);
		map.put(1, null);
		map.put(2, null);
		map.put(3, null);
		map.put(4, null);
		map.put(5, null);

		Set<Integer> keys = map.keySet();
		assertEquals("[1, 2, 3, 4, 5]", keys.toString());

		map.put(6, null);
		assertEquals("[2, 3, 4, 5, 6]", keys.toString());

		map.put(7, null);
		assertEquals("[3, 4, 5, 6, 7]", keys.toString());

		map.put(8, null);
		assertEquals("[4, 5, 6, 7, 8]", keys.toString());
	}


	public static class LimitedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
		private final int maxSize;

		public LimitedLinkedHashMap(int maxSize) {
			super(16, .75f, true);
			this.maxSize = maxSize;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > maxSize;
		}

	}

}
