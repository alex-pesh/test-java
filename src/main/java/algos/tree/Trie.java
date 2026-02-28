package algos.tree;

import org.openjdk.jol.vm.VM;

public class Trie {
	private Node root;

	public Trie() {
		root = new Node((char) 0, null);
	}


	public void insert(String key, Object value) {

		char[] chars = key.toCharArray();
		Node current = root;

		for (char c : chars) {
			int index = c - 'a';
			if (current.children[index] == null) {
				current.children[index] = new Node(c, value);
			}
			current = current.children[index];
		}

		current.isLeaf = true;
	}


	public Object search(String key, boolean exactMatch) {

		char[] chars = key.toCharArray();
		Node current = root;

		for (char c : chars) {
			int index = c - 'a';
			if (current.children[index] == null) {
				return null;
			}
			current = current.children[index];
		}

		return (!exactMatch || current.isLeaf) ? current.value : null;
	}


	public boolean isPrefix(String key) {

		char[] chars = key.toCharArray();
		Node current = root;

		for (char c : chars) {
			int index = c - 'a';
			if (current.children[index] == null) {
				return false;
			}
			current = current.children[index];
		}

		return true;
	}


	private static class Node {
		private char key;
		private Object value;
		private Node[] children;
		boolean isLeaf;

		public Node(char key, Object value) {
			this.key = key;
			this.value = value;
			this.children = new Node[32];
		}
	}


	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.insert("dictionary", "DICTIONARY");
		trie.insert("directory", "DIRECTORY");
		trie.insert("dictator", "DICTATOR");
		trie.insert("dark", "DARK");
		trie.insert("dash", "DASH");
		trie.insert("dashboard", "DASHBOARD");

		String[] typings = new String[] {
				"d", "di", "di", "dic", "dict", "dicta", "dictator", "dir", "da", "dar", "das", "dash", "dashb"
		};

		int maxLen = 1;
		for (String typing : typings) {
			maxLen = Math.max(maxLen, typing.length());
		}

		for (String typing : typings) {
			System.out.printf("%1$-" + maxLen + "s -> %2$s%n", typing, trie.search(typing, false));
		}
	}

}
