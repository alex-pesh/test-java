package algos.tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.function.BiConsumer;

/**
 * https://ru.wikipedia.org/wiki/%D0%9E%D0%B1%D1%85%D0%BE%D0%B4_%D0%B4%D0%B5%D1%80%D0%B5%D0%B2%D0%B0
 * https://habr.com/ru/articles/65617/
 * https://habr.com/ru/articles/144850/
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

	public static void main(String[] args) {

		var tree = new BinarySearchTree<Integer, String>()
						.add(10, "")
						.add(5, "")
						.add(1, "")
						.add(4, "")
						.add(35, "")
						.add(20, "")
						.add(99, "")
						.add(17, "")
						.add(31, "")
						.add(95, "")
						.add(42, "");

//		System.out.println(tree.get(31));

		BiConsumer<Integer, String> consumer = (k, v) -> System.out.print(k + " — ");

		System.out.println("Breadth First: ");
		tree.breadthFirstSearch(consumer);
		System.out.println();

		System.out.println("Deep First Preorder (NLR): ");
		tree.deepFirstNLRSearch(consumer);
		System.out.println();

		System.out.println("Deep First Inorder (LNR): ");
		tree.deepFirstLNRSearch(consumer);
		System.out.println();

		System.out.println("Deep First Postorder (LRN): ");
		tree.deepFirstLRNSearch(consumer);
	}




	private Node<K, V> root;
	private int size = 0;

	public BinarySearchTree<K, V> add(K key, V value) {
		Node<K, V> leaf = this.root, parent = null;
		int compared = 0;
		size++;

		while (leaf != null) {
			compared = key.compareTo(leaf.key);
			if (compared == 0) {
				leaf.value = value;
				return this;
			}
			parent = leaf;
			if (compared < 0) {
				leaf = leaf.left;
			} else {
				leaf = leaf.right;
			}
		}

		Node<K, V> node = new Node<>(key, value);
		if (root == null) {
			root = node;
			return this;
		}

		if (compared < 0) {
			parent.left = node;
		} else {
			parent.right = node;
		}

		return this;
	}


	public V get(K key) {
		if (this.root == null) {
			return null;
		}

		Node<K, V> leaf = this.root;

		while (leaf != null) {
			int compared = key.compareTo(leaf.key);
			if (compared == 0) {
				return leaf.value;
			}

			leaf = compared < 0 ? leaf.left : leaf.right;
		}

		return null;
	}


	/**
	 * Обход дерева в ширину.
	 * Подходит, если нужно найти самый короткий путь или когда решение находится близко к корню; предотвращает уход в глубокие ветви.
	 */
	public void breadthFirstSearch(BiConsumer<K, V> consumer) {
		if (this.root == null) {
			return;
		}

		Node<K, V> node = this.root;
		Queue<Node<K, V>> queue = new ArrayDeque<>();

		do {
			consumer.accept(node.key, node.value);
			if (node.left != null) {
				queue.add(node.left);
			}
			if (node.right != null) {
				queue.add(node.right);
			}
		} while ((node = queue.poll()) != null);
	}


	/**
	 * Обход в глубину прямой (Preorder, NLR): Корень → Левое поддерево → Правое поддерево.
	 * <pre>
	 * - Проверяем, не является ли текущий узел пустым или null.
	 * - Показываем поле данных корня (или текущего узла).
	 * - Обходим левое поддерево рекурсивно, вызвав функцию прямого обхода.
	 * - Обходим правое поддерево рекурсивно, вызвав функцию прямого обхода.
	 * </pre>
	 */
	public void deepFirstNLRSearch(BiConsumer<K, V> consumer) {
		if (this.root == null) {
			return;
		}

		Node<K, V> node = this.root;
		Deque<Node<K, V>> stack = new ArrayDeque<>();

		stack.push(node);

		while (!stack.isEmpty()) {
			node = stack.pop();
			consumer.accept(node.key, node.value);
			if (node.right != null) {
				stack.push(node.right);
			}
			if (node.left != null) {
				stack.push(node.left);
			}
		}
	}

	public void deepFirstNLRSearchRecursive(BiConsumer<K, V> consumer) {
		preorder(root, consumer);
	}

	void preorder(Node<K, V> node, BiConsumer<K, V> consumer) {
		if (node == null) {
			return;
		}
		consumer.accept(node.key, node.value);
		preorder(node.left, consumer);
		preorder(node.right, consumer);
	}


	/**
	 * Обход в глубину центрированный (Inorder, LNR): Левое поддерево → Корень → Правое поддерево
	 * (дает отсортированный порядок для деревьев поиска).
	 * <pre>
	 * - Проверяем, не является ли текущий узел пустым или null.
	 * - Обходим левое поддерево рекурсивно, вызвав функцию центрированного обхода.
	 * - Показываем поле данных корня (или текущего узла).
	 * - Обходим правое поддерево рекурсивно, вызвав функцию центрированного обхода.
	 * </pre>
	 */
	public void deepFirstLNRSearch(BiConsumer<K, V> consumer) {
		if (this.root == null) {
			return;
		}

		Deque<Node<K, V>> stack = new ArrayDeque<>();
		Node<K, V> node = this.root;

		while (!stack.isEmpty() || node != null) {
			if (node != null) {
				stack.push(node);
				node = node.left;
			} else {
				node = stack.pop();
				consumer.accept(node.key, node.value);
				node = node.right;
			}
		}
	}


	public void deepFirstLNRSearchRecursive(BiConsumer<K, V> consumer) {
		inorder(root, consumer);
	}

	void inorder(Node<K, V> node, BiConsumer<K, V> consumer) {
		if (node == null) {
			return;
		}
		inorder(node.left, consumer);
		consumer.accept(node.key, node.value);
		inorder(node.right, consumer);
	}


	/**
	 * Обход в глубину обратный (Postorder, LRN): Левое поддерево → Правое поддерево → Корень.
	 * <pre>
	 * - Проверяем, не является ли текущий узел пустым или null.
	 * - Обходим левое поддерево рекурсивно, вызвав функцию обратного обхода.
	 * - Обходим правое поддерево рекурсивно, вызвав функцию обратного обхода.
	 * - Показываем поле данных корня (или текущего узла).
	 * </pre
	 */
	public void deepFirstLRNSearch(BiConsumer<K, V> consumer) {
		if (this.root == null) {
			return;
		}

		Deque<Node<K, V>> stack = new ArrayDeque<>();
		Node<K, V> resentNode = null, node = this.root;

		while (!stack.isEmpty() || node != null) {
			if (node != null) {
				stack.push(node);
				node = node.left;
			} else {
				var peekNode = stack.peek();
				// если правый потомок существует и обход пришёл из левого потомка, двигаемся вправо
				if (peekNode.right != null && resentNode != peekNode.right) {
					node = peekNode.right;
				} else {
					consumer.accept(peekNode.key, peekNode.value);
					resentNode = stack.pop();
				}
			}
		}

	}


	public void deepFirstLRNSearchRecursive(BiConsumer<K, V> consumer) {
		postorder(root, consumer);
	}

	void postorder(Node<K, V> node, BiConsumer<K, V> consumer) {
		if (node == null) {
			return;
		}
		postorder(node.left, consumer);
		postorder(node.right, consumer);
		consumer.accept(node.key, node.value);
	}



	static class Node<K, V> {
		private K key;
		private V value;
		Node<K, V> left, right;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key.toString();
		}
	}

}
