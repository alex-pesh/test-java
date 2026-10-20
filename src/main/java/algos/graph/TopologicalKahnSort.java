package algos.graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://en.wikipedia.org/wiki/Topological_sorting
 * https://dev.to/faangmaster/topologhichieskaia-sortirovka-4eee
 * https://habr.com/ru/companies/otus/articles/499138/
 */
public class TopologicalKahnSort {

	public static void main(String[] args) {
		Map<Integer, Set<Integer>> graph = new HashMap<>();
		graph.put(1, null);
		graph.put(2, Set.of(1, 3, 4));
		graph.put(3, Set.of(1, 4));
		graph.put(4, null);

		List<Integer> sorted = sort(graph);
		System.out.println(sorted);
	}


	public static List<Integer> sort(Map<Integer, Set<Integer>> deps) {

		List<Integer> result = new ArrayList<>();
		while (!deps.isEmpty()) {
			Iterator<Map.Entry<Integer, Set<Integer>>> iterator = deps.entrySet().iterator();
			Integer removed = null;
			while (iterator.hasNext()) {
				Map.Entry<Integer, Set<Integer>> entry = iterator.next();
				if (entry.getValue() == null || entry.getValue().isEmpty()) {
					iterator.remove();
					result.add(removed = entry.getKey());
				}
			}

			if (removed == null) {
				throw new RuntimeException("Dependency loop is detected: " + deps);
			}

			deps.replaceAll((key, set) -> {
				return set.stream().filter(value -> !result.contains(value)).collect(Collectors.toSet());
			});
		}

		return result;
	}

}
