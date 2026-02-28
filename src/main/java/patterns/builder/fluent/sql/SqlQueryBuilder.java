
package patterns.builder.fluent.sql;

public class SqlQueryBuilder implements Select, From, Where, OrderBy {

	public static void main(String[] args) {
		String sql = new SqlQueryBuilder()
				.select("t.column_1", "column_2", "column_3")
				.from("table_name t")
				.where("column_2 > 0")
				.orderBy("column_1")
				.build();

		System.out.printf("Sql: %s", sql);

	}


	private final StringBuilder builder = new StringBuilder();

	@Override
	public From select(String... columns) {
		builder.append("SELECT ").append(String.join(", ", columns));
		return this;
	}

	@Override
	public Where from(String table) {
		builder.append(" FROM ").append(table);
		return this;
	}

	@Override
	public OrderBy where(String condition) {
		builder.append(" WHERE ").append(condition);
		return this;
	}

	@Override
	public SqlQueryBuilder orderBy(String... columns) {
		builder.append(" ORDER BY ").append(String.join(", ", columns));
		return this;
	}


	public String build () {
		return builder.toString();
	}

}
