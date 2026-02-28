package patterns.decorator.ex1;

// https://www.youtube.com/watch?v=2aB2B3b3bQA&list=PLlsMRoVt5sTPgGbinwOVnaF1mxNeLAD7P&index=14

public class DecoratorTest {

	public static void main(String[] args) {
		Developer developer = new TeamLeadDeveloper(new SeniorJavaDeveloper(new JavaDeveloper()));

		System.out.println(developer.makeJob());
	}

}
