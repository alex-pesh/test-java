package algos.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BreadthFirstSearch {

	private final Graph graph;
	private final Consumer<Integer> consumer;

	public BreadthFirstSearch(Graph graph, Consumer<Integer> consumer) {
		this.graph = graph;
		this.consumer = consumer;
	}


	public void traceFrom(int vertex) {
		if (vertex > graph.verticesCount()) {
			return;
		}

		Deque<Integer> queue = new ArrayDeque<>();
		boolean[] marked = new boolean[graph.verticesCount()];

		queue.add(vertex);
		marked[vertex] = true;

		while (!queue.isEmpty()) {
			vertex = queue.remove();
			consumer.accept(vertex);
			for (Integer adjVertex : graph.adjacency(vertex)) {
				if (!marked[adjVertex]) {
					marked[adjVertex] = true;
					queue.add(adjVertex);
				}
			}
		}
	}
}
