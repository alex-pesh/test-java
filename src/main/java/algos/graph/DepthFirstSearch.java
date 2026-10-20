package algos.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DepthFirstSearch {

	private final Graph graph;
	private final Consumer<Integer> consumer;

	public DepthFirstSearch(Graph graph, Consumer<Integer> consumer) {
		this.graph = graph;
		this.consumer = consumer;
	}


	public void traceFrom(int vertex) {
		if (vertex > graph.verticesCount()) {
			return;
		}

		Deque<Integer> stack = new ArrayDeque<>();
		boolean[] marked = new boolean[graph.verticesCount()];

		stack.push(vertex);
		marked[vertex] = true;

		while (!stack.isEmpty()) {
			vertex = stack.pop();
			consumer.accept(vertex);
			for (Integer adjVertex : graph.adjacency(vertex).reversed()) {
				if (!marked[adjVertex]) {
					marked[adjVertex] = true;
					stack.push(adjVertex);
				}
			}
		}
	}

}
