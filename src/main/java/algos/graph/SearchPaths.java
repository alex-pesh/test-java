package algos.graph;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SearchPaths {

	public static void main(String[] args) {

/*
		Graph graph = new Graph(10)
				.addEdge(0, 1)
				.addEdge(0, 2)
				.addEdge(0, 3)
				.addEdge(3, 1)
				.addEdge(3, 4)
				.addEdge(3, 6)
				.addEdge(5, 4)
				.addEdge(5, 6)
				.addEdge(5, 7)
				.addEdge(6, 7)
				.addEdge(6, 8)
				.addEdge(6, 9);
*/
		Graph graph = new Graph(7)
				.addEdge(0, 1)
				.addEdge(0, 2)
				.addEdge(1, 3)
				.addEdge(1, 4)
				.addEdge(2, 5)
				.addEdge(2, 6);


		Consumer<Integer> consumer = (vertex) -> System.out.print(vertex + " - ");

		System.out.println("Input graph: ");
		System.out.println(graph.toString());

		DepthFirstSearch dfs = new DepthFirstSearch(graph, consumer);
		BreadthFirstSearch bfs = new BreadthFirstSearch(graph, consumer);

		System.out.println("DFS:");
		dfs.traceFrom(0);

		System.out.println();
		System.out.println("BFS:");
		bfs.traceFrom(0);

	}

}
