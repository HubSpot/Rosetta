package com.hubspot.rosetta.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.hubspot.rosetta.RosettaMapper;

public class RosettaMapperFactory implements ResultSetMapperFactory {
  private static final Set<Class<?>> BLACKLIST = new HashSet<Class<?>>(Arrays.asList(
          Byte.class,
          Short.class,
          Integer.class,
          Long.class,
          Float.class,
          Double.class,
          Boolean.class,
          Character.class,
          String.class,
          java.util.Date.class,
          java.sql.Date.class,
          java.sql.Timestamp.class
  ));

  private final ObjectMapper om;

  public RosettaMapperFactory(){
        om = Rosetta.getMapper();
  }

  public RosettaMapperFactory(ObjectMapper om) {
      this.om = om;

  }

  @Override
  public boolean accepts(@SuppressWarnings("rawtypes") Class type, StatementContext ctx) {
    return !(type.isPrimitive() || type.isArray() || type.isAnnotation() || BLACKLIST.contains(type));
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
    final RosettaMapper mapper = new RosettaMapper(type, om, extractTableName(ctx.getRewrittenSql()));

    return new ResultSetMapper() {

      @Override
      public Object map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return mapper.mapRow(r);
      }
    };
  }

  private String extractTableName(final String sql) {
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

    int endTableIndex = tableString.indexOf(' ');

    return endTableIndex < 0 ? tableString : tableString.substring(0, endTableIndex);
  }
}
