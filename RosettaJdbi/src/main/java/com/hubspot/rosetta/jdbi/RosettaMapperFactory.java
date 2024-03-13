package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.RosettaMapper;
import com.hubspot.rosetta.util.SqlTableNameExtractor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.BuiltInArgumentFactory;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class RosettaMapperFactory implements ResultSetMapperFactory {

  @Override
  public boolean accepts(@SuppressWarnings("rawtypes") Class type, StatementContext ctx) {
    return !(
      type.isPrimitive() ||
      type.isArray() ||
      type.isAnnotation() ||
      BuiltInArgumentFactory.canAccept(type)
    );
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ResultSetMapper mapperFor(Class rawType, StatementContext ctx) {
    ObjectMapper objectMapper = RosettaObjectMapperOverride.resolve(ctx);

    final Type genericType;
    if (ctx.getSqlObjectMethod() == null) {
      genericType = rawType;
    } else {
      genericType =
        determineGenericReturnType(
          rawType,
          ctx.getSqlObjectMethod().getGenericReturnType()
        );
    }
    String tableName = SqlTableNameExtractor.extractTableName(ctx.getRewrittenSql());
    final RosettaMapper mapper = new RosettaMapper(genericType, objectMapper, tableName);

    return new ResultSetMapper() {
      @Override
      public Object map(int index, ResultSet r, StatementContext ctx)
        throws SQLException {
        return mapper.mapRow(r);
      }
    };
  }

  static Type determineGenericReturnType(Class rawType, Type returnType) {
    if (rawType == returnType || !(returnType instanceof ParameterizedType)) {
      return rawType;
    } else {
      ParameterizedType parameterizedType = (ParameterizedType) returnType;

      if (rawType == parameterizedType.getRawType()) {
        return parameterizedType;
      } else if (parameterizedType.getActualTypeArguments().length == 1) {
        return determineGenericReturnType(
          rawType,
          parameterizedType.getActualTypeArguments()[0]
        );
      } else {
        return rawType;
      }
    }
  }
}
