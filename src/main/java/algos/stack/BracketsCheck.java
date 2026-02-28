package algos.stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *	https://leetcode.com/problems/valid-parentheses/
 */
public class BracketsCheck {

	public static void main(String[] args) {
		boolean isValid = new BracketsCheck().validate("void test() {}");
		System.out.println("Is valid: " + isValid);
	}


	char[] openingChars = new char[] {'(', '{', '['};
	char[] closingChars = new char[] {')', '}', ']'};

	private boolean isOpening(char c) {
		for (char opening : openingChars) {
			if (c == opening) return true;
		}
		return false;
	}

	private int openingIdx(char c) {
		for (int i = 0; i < openingChars.length; i++) {
			if (c == openingChars[i]) return i;
		}
		return -1;
	}

	private boolean isClosing(char c) {
		for (char closing : closingChars) {
			if (c == closing) return true;
		}
		return false;
	}

	private int closingIdx(char c) {
		for (int i = 0; i < closingChars.length; i++) {
			if (c == closingChars[i]) return i;
		}
		return -1;
	}


	public boolean validate(String s) {
		Deque<Character> stack = new ArrayDeque<>();

		char[] array = s.toCharArray();
		for (int i = 0; i < array.length; i++) {
			char c = array[i];
			int openingIdx = openingIdx(c);

			if (openingIdx >= 0) {
				stack.push(c);
			} else if (closingIdx(c) >= 0) {
				Character popped = stack.poll();
				if (popped == null) {
					throw new RuntimeException(String.format("Error: no opening brace for '%c' at index %d", c, i));
				}

				int poppedIdx = openingIdx(popped);
				if (closingChars[poppedIdx] != c) {
					throw new RuntimeException(String.format("Error: no opening brace for '%c' at index %d", c, i));
				}
			}
		}

		if (!stack.isEmpty()) {
			throw new RuntimeException(String.format("Error: no closing brace for '%c'", stack.peek()));
		}

		return true;
	}

}
