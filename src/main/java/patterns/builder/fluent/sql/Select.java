package patterns.builder.fluent.sql;

public interface Select {
	From select(String... columns);
}
