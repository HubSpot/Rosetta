package com.hubspot.rosetta.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SqlTableNameExtractorTest {

  @Test
  public void itExtractsTableNameBasic() {
    String sql = "SELECT * FROM table WHERE bar = baz";

    assertThat(SqlTableNameExtractor.extractTableName(sql)).isEqualTo("table");
  }

  @Test
  public void itExtractsTableNameWithComma() {
    String sql = "SELECT * FROM table, other_table WHERE bar = baz";

    assertThat(SqlTableNameExtractor.extractTableName(sql)).isEqualTo("table");
  }

  @Test
  public void itExtractsTableNameWithSemicolon() {
    String sql = "SELECT * FROM table;";

    assertThat(SqlTableNameExtractor.extractTableName(sql)).isEqualTo("table");
  }
}
