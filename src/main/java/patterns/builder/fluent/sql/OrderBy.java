package patterns.builder.fluent.sql;

public interface OrderBy {
	SqlQueryBuilder orderBy(String... columns);
}
