package algos.graph;

import java.util.*;

public class Graph {

	private volatile int edges;
	private List<List<Integer>> adjList; // adjacency lists


	public Graph(int vertices) {
		this.edges = 0;
		this.adjList = new ArrayList<>(vertices);
		for (int i = 0; i < vertices; i++) {
			adjList.add(i, new ArrayList<>());
		}
	}

	public int edgesCount() {
		return edges;
	}

	public int verticesCount() {
		return adjList.size();
	}


	public Graph addEdge(int v1, int v2) {
		adjList.get(v1).add(v2);
		adjList.get(v2).add(v1);
		edges++;

		return this;
	}


	public List<Integer> adjacency(int vertex) {
		return adjList.get(vertex);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		for (List<Integer> list : adjList) {
			builder.append(counter++).append(" -> ");
			for (Integer vertex : list) {
				builder.append(vertex).append(", ");
			}
			builder.append(System.lineSeparator());
		}

		return builder.toString();
	}
}
