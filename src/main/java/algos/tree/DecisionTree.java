package algos.tree;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * https://medium.com/@nirmal1067/decision-tree-from-scratch-5c39498ebe02
 * https://medium.com/@oliver.tazl/building-a-flexible-decision-tree-in-java-using-predicates-and-priority-handling-d6f8e7fb666c
 * https://github.com/paschistrobel/DecisionTree-Java-Implementation
 */
public class DecisionTree {


	public static void main(String[] args) {

	}


	static class Node<T, R> {

		private Node<T, R> left;
		private Node<T, R> right;
		private Predicate<T> predicate;
		private Function<T, R> function;


		public R accept(T input) {
			if (predicate.test(input)) {
				return accept(input, right);
			} else {
				return accept(input, left);
			}
		}

		private R accept(T input, Node<T, R> node) {
			if (node == null) {
				return function.apply(input);
			} else {
				return node.accept(input);
			}
		}

	}

}
