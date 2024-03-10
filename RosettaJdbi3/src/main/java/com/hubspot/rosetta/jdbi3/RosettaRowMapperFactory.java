package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.RosettaMapper;
import com.hubspot.rosetta.util.SqlTableNameExtractor;
import java.lang.reflect.Type;
import java.util.Optional;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.generic.GenericTypes;
import org.jdbi.v3.core.mapper.ColumnMappers;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.mapper.RowMapperFactory;

public class RosettaRowMapperFactory implements RowMapperFactory {

  @Override
  public Optional<RowMapper<?>> build(Type type, ConfigRegistry config) {
    if (accepts(type, config)) {
      return Optional.of((rs, ctx) -> {
        ObjectMapper objectMapper = ctx
          .getConfig(RosettaObjectMapper.class)
          .getObjectMapper();
        String tableName = SqlTableNameExtractor.extractTableName(
          ctx.getParsedSql().getSql()
        );
        final RosettaMapper mapper = new RosettaMapper(type, objectMapper, tableName);

        return mapper.mapRow(rs);
      });
    } else {
      return Optional.empty();
    }
  }

  private static boolean accepts(Type type, ConfigRegistry config) {
    Class<?> rawType = GenericTypes.getErasedType(type);

    if (rawType.isPrimitive() || rawType.isArray() || rawType.isAnnotation()) {
      return false;
    } else if (rawType == Optional.class) {
      Optional<Type> optionalType = GenericTypes.findGenericParameter(
        type,
        Optional.class
      );
      // TODO we currently return false if we can't infer the type of the Optional, should we reverse that?
      return optionalType.isPresent() && accepts(optionalType.get(), config);
    } else {
      return !hasBuiltInMapper(type, config);
    }
  }

  private static boolean hasBuiltInMapper(Type type, ConfigRegistry config) {
    return config.get(ColumnMappers.class).findFor(type).isPresent();
  }
}
