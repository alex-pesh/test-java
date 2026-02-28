package patterns.decorator.ex1;

public class SeniorJavaDeveloper extends DeveloperDecorator {

	public SeniorJavaDeveloper(Developer developer) {
		super(developer);
	}

	private String makeCodeReview() {
		return "Make code review. ";
	}

	@Override
	public String makeJob() {
		return super.makeJob() + makeCodeReview();
	}

}
