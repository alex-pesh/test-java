package core.sealed;

public sealed interface Employee permits Salaried, Freelancer {

}
