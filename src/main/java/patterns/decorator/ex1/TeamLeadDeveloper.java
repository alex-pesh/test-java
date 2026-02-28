package patterns.decorator.ex1;

public class TeamLeadDeveloper extends DeveloperDecorator {

	public TeamLeadDeveloper(Developer developer) {
		super(developer);
	}

	private String makeReport() {
		return "Make report. ";
	}

	@Override
	public String makeJob() {
		return super.makeJob() + makeReport();
	}

}
