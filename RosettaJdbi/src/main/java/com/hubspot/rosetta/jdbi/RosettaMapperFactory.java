package com.hubspot.rosetta.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.skife.jdbi.v2.BuiltInArgumentFactory;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.hubspot.rosetta.RosettaMapper;

public class RosettaMapperFactory implements ResultSetMapperFactory {

  @Override
  public boolean accepts(@SuppressWarnings("rawtypes") Class type, StatementContext ctx) {
    return !(type.isPrimitive() || type.isArray() || type.isAnnotation() || BuiltInArgumentFactory.canAccept(type));
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
    ObjectMapper objectMapper = RosettaObjectMapperOverride.resolve(ctx);
    final RosettaMapper mapper = new RosettaMapper(type, objectMapper, extractTableName(ctx.getRewrittenSql()));

    return new ResultSetMapper() {

      @Override
      public Object map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return mapper.mapRow(r);
      }
    };
  }

  static String extractTableName(final String sql) {
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
