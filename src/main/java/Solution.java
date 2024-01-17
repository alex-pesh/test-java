import java.util.HashMap;
import java.util.Map;


/**
 *	https://leetcode.com/problems/valid-parentheses/
 */
class Solution {

	private static final Map<Character, Character> map = new HashMap<>();
	static {
		map.put('{', '}');
		map.put('[', ']');
		map.put('(', ')');
	}

	public static boolean isValid(String s) {

		Map<Character, Integer> charCounter = new HashMap<>();
		for (char key : s.toCharArray()) {
			Character value = map.get(key);
			if (value != null) {
				charCounter.merge(key, 1, (oldValue, newValue) -> newValue + oldValue);
			}
		}

		System.out.println(charCounter);

		return true;
	}
}