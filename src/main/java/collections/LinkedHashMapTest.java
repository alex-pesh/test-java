package collections;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTest {


	public static void test() {

		// Example with accessOrder and removing of eldest entries
		Map<Integer, Object> map = new LinkedHashMap<>(16, .75f, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<Integer, Object> eldest) {
				return size() > 500;
			}
		};

		for (int i = 1; i <= 50; i++) {
			map.put(i, i);
		}

		map.get(1);
		map.get(6);

		for (Map.Entry entry : map.entrySet()) {
			System.out.println(entry.getKey());
		}

	}

}
