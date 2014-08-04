package com.hubspot.rosetta.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.RosettaMapper;
import com.hubspot.rosetta.RosettaMapperFactory;

public class RosettaResultSetMapperFactory implements ResultSetMapperFactory {

  @Override
  public boolean accepts(@SuppressWarnings("rawtypes") Class type, StatementContext ctx) {
    return Rosetta.tabletForType(type) != null;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
    final RosettaMapper ourMapper = RosettaMapperFactory.forType(type);
    return new ResultSetMapperProxy(ourMapper);
  }

  public static <T> ResultSetMapper<T> mapperFor(Class<T> type) {
    final RosettaMapper<T> ourMapper = RosettaMapperFactory.forType(type);
    return new ResultSetMapperProxy<T>(ourMapper);
  }

}

class ResultSetMapperProxy<T> implements ResultSetMapper<T> {

  private RosettaMapper<T> mapper;

  public ResultSetMapperProxy(RosettaMapper<T> mapper) {
    this.mapper = mapper;
  }

  @Override
  public T map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    return mapper.mapRow(r);
  }
}
