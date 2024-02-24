package algos.sorting;

import java.util.HashMap;
import java.util.Map;

public class BracketsCheck {

	public static void test() {
		Map<Character, CharDefinition> map = new HashMap<>();
		CharDefinition.put('{', '}', map);
		CharDefinition.put('(', ')', map);
		CharDefinition.put('[', ']', map);

	}



	public static class CharDefinition {
		Character character, pair;
		boolean isOpening;

		public CharDefinition(Character character, Character pair, boolean isOpening) {
			this.character = character;
			this.pair = pair;
			this.isOpening = isOpening;
		}

		public static void put(Character character, Character pair, Map<Character, CharDefinition> map) {
			CharDefinition definition = new CharDefinition(character, pair, true);
			CharDefinition pairDefinition = new CharDefinition(definition.pair, definition.character, false);
			map.put(definition.character, definition);
			map.put(definition.pair, pairDefinition);
		}
	}

}
