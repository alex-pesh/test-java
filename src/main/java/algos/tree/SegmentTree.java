package algos.tree;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * https://habr.com/ru/articles/808511/
 * https://www.youtube.com/watch?v=99M67y9kLJs
 * http://e-maxx.ru/algo/segment_tree
 */
public class SegmentTree {

	private final int[] t;
	private int n;

	public SegmentTree(int[] array) {
		// массив, хранящий дерево должен иметь размер следующей степени двойки от размера входного
		int size = (int) Math.pow(2, log2(array.length-1) + 1);
		this.t = new int[size];
		// входной массив должен быть дополнен нейтральными (для вычисления сумм - нулями) значениями до размера кратного 2
		int new_size = (int) Math.pow(2, log2(array.length));
		array = Arrays.copyOf(array, new_size);
		build(array, 0, 0, array.length);
	}

	private static int log2(int value) {
		return (int) Math.ceil(Math.log(value) / Math.log(2));
	}

	private void build(int[] a, int v, int tl, int tr) {
		if (tl == tr - 1) {
			t[v] = a[tl];
		} else {
			int tm = tl + (tr - tl)/2;
			build(a, v * 2 + 1, tl, tm);
			build(a, v * 2 + 2, tm, tr);
			t[v] = t[v * 2 + 1] + t[v * 2 + 2];
		}
	}


	public long sum(int l, int r, int x, int xl, int xr) {
		if (r <= xl || l >= xr) {
			return 0;
		}
		if (xl >= l && xr <= r) {
			return t[x];
		}
		int xm = (xl + xr) / 2;
		return sum(l, r, 2 * x + 1, xl, xm) + sum(l, r, 2 * x + 2, xm, xr);
	}


	@Override
	public String toString() {
		return Arrays.toString(t);
	}



	public static void main(String[] args) {

		int[] array = new int[] {5, 2, 3, 3};
		SegmentTree tree = new SegmentTree(array);

		System.out.println(tree);
	}

}
