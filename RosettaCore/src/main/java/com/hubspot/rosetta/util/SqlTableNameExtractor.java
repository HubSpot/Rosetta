package com.hubspot.rosetta.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class SqlTableNameExtractor {

  @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
  private SqlTableNameExtractor() {
    throw new AssertionError();
  }

  public static String extractTableName(final String sql) {
    String lowerCaseSql = sql.toLowerCase();

    String from = " from ";
    int fromIndex = lowerCaseSql.indexOf(from);
    if (fromIndex < 0) {
      return null;
    }

    String tableString = sql.substring(fromIndex + from.length());
    if (tableString.startsWith("(")) {
      return null;
    }

    int endTableIndex = -1;
    for (int i = 0; i < tableString.length(); i++) {
      char c = tableString.charAt(i);
      if (c == ' ' || c == ',' || c == ';') {
        endTableIndex = i;
        break;
      }
    }

    return endTableIndex < 0 ? tableString : tableString.substring(0, endTableIndex);
  }
}
