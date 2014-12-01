package com.hubspot.rosetta.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hubspot.rosetta.Rosetta;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.hubspot.rosetta.RosettaMapper;

public class RosettaMapperFactory implements ResultSetMapperFactory {
  private static final Map<Class<?>, Boolean> CACHE = new ConcurrentHashMap<Class<?>, Boolean>();
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

  @Override
  public boolean accepts(@SuppressWarnings("rawtypes") Class type, StatementContext ctx) {
    Boolean cached = CACHE.get(type);
    if (cached != null) {
      return cached;
    }

    final boolean accepts;
    if (type.isPrimitive() || type.isArray() || type.isAnnotation() || BLACKLIST.contains(type)) {
      accepts = false;
    } else {
      accepts = looksLikeJsonObject(type);
    }

    CACHE.put(type, accepts);
    return accepts;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
    final RosettaMapper mapper = new RosettaMapper(type);

    return new ResultSetMapper() {

      @Override
      public Object map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return mapper.mapRow(r);
      }
    };
  }

  private static boolean looksLikeJsonObject(Class<?> type) {
    try {
      ObjectNode schema = Rosetta.getMapper().generateJsonSchema(type).getSchemaNode();
      return "object".equals(schema.get("type").textValue());
    } catch (Exception e) {
      return false;
    }
  }
}