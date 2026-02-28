package patterns.visitor.v2;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class VisitorTest_v2 {


	public static void main(String[] args) throws Throwable {

		Root element = new D()
				.addChild(new B().addChild(new C().addChild(new A())))
				.addChild(new A());

		Visitor<Root, TreeCounterResult> visitor = new TreeLabelVisitor<>();
		VisitResult result = visitor.visit(element);

		System.out.println("Total elements: " + result);
	}




	interface Visitor<T, R extends VisitResult> {
		R visit(T target);
	}

	interface VisitResult {

	}


	static class NameAdjusterVisitor<T extends Root> implements Visitor<T, VisitResult> {
		@Override
		public VisitResult visit(T target) {
			System.out.println( "_____ " + target.getName() + " ____");
			return null;
		}
	}


	@Getter
	static class TreeCounterResult implements VisitResult {
		private int counter = 0;

		public void increment() {
			counter++;
		}

		@Override
		public String toString() {
			return String.valueOf(counter);
		}
	}


	static class TreeLabelVisitor<T extends Root> implements Visitor<T, TreeCounterResult> {
		StringBuilder builder = new StringBuilder();
		TreeCounterResult result = new TreeCounterResult();

		@Override
		public TreeCounterResult visit(T target) {
			appendLabel(target, 0);
			System.out.println(builder.toString());

			return result;
		}

		private void appendLabel(Root target, int level) {
			result.increment();
			if (level++ > 0) {
				builder.append(" ");
			}
			builder.append(target.getName());
			if (target.getChildren() == null || target.getChildren().isEmpty()) {
				return;
			}

			for (Root child : target.getChildren()) {
				builder.append(System.lineSeparator()).append("└")/*.append("├")*/;
				builder.append("─".repeat(level));
				appendLabel(child, level);
			}
		}
	}


	static abstract class Root {
		private final List<Root> children = new ArrayList<>();

		public Root addChild(Root child) {
			children.add(child);
			return this;
		}

		public List<Root> getChildren() {
			return children;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName();
		}

		public abstract String getName();
	}



	static class A extends Root {
		public String getName() {
			return "A";
		}
	}

	static class B extends A {
		public String getName() {
			return "B";
		}
	}

	static class C extends B {
		public String getName() {
			return "C";
		}
	}

	static class D extends Root {
		public String getName() {
			return "D";
		}
	}

}
