package com.hubspot.rosetta.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.RosettaMapper;

public class RosettaMapperFactory implements ResultSetMapperFactory {

  @Override
  public boolean accepts(@SuppressWarnings("rawtypes") Class type, StatementContext ctx) {
    try {
      JsonSerializer<Object> serializer = Rosetta.getMapper().getSerializerProvider().findValueSerializer(type, null);
      return serializer != null && !UnknownSerializer.class.equals(serializer.getClass());
    } catch (JsonMappingException e) {
      return false;
    }
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
}