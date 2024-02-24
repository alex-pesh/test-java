package core.sealed;

public class Payment {

	public void pay(Employee employee) {

		switch (employee) {
			case Salaried(var name, var payGrade) -> paySalary(payGrade);
			case Freelancer(var name, var hourlyRate) when hourlyRate > 250 -> payInvoice(hourlyRate);
			default -> {}
		}
	}

	protected void paySalary(Salaried.PayGrade payGrade) {

	}

	protected void payInvoice(double hourlyRate) {

	}

}
