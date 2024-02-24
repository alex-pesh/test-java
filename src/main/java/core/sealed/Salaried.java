package core.sealed;

public record Salaried(String name, PayGrade payGrade) implements Employee {


	public static class PayGrade {}

}
