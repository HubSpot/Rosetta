package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.internal.RosettaModule;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.StatementContext;

public class RosettaObjectMapperOverride {
  public static final String RESULTSET_MAPPER_ATTRIBUTE_NAME = "_rosetta_object_mapper_resultset_mapper";
  public static final String STATEMENT_BINDER_ATTRIBUTE_NAME = "_rosetta_object_mapper_statement_binder";

  private final ObjectMapper objectMapper;

  public RosettaObjectMapperOverride(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper.copy().registerModule(new RosettaModule());
  }

  public void override(DBI dbi) {
    dbi.define(STATEMENT_BINDER_ATTRIBUTE_NAME, objectMapper);
    dbi.define(RESULTSET_MAPPER_ATTRIBUTE_NAME, objectMapper);

  }
  public void overrideResultSetMapper(DBI dbi) {
    dbi.define(RESULTSET_MAPPER_ATTRIBUTE_NAME, objectMapper);
  }
  public void overrideStatementBinder(DBI dbi) {
    dbi.define(STATEMENT_BINDER_ATTRIBUTE_NAME, objectMapper);
  }

  public void override(Handle handle) {
    handle.define(STATEMENT_BINDER_ATTRIBUTE_NAME, objectMapper);
    handle.define(RESULTSET_MAPPER_ATTRIBUTE_NAME, objectMapper);  }
  public void overrideResultSetMapper(Handle handle) {
    handle.define(RESULTSET_MAPPER_ATTRIBUTE_NAME, objectMapper);
  }
  public void overrideStatementBinder(Handle handle) {
    handle.define(STATEMENT_BINDER_ATTRIBUTE_NAME, objectMapper);
  }

  public void override(SQLStatement<?> statement) {
    statement.define(STATEMENT_BINDER_ATTRIBUTE_NAME, objectMapper);
    statement.define(RESULTSET_MAPPER_ATTRIBUTE_NAME, objectMapper);  }
  public void overrideResultSetMapper(SQLStatement<?> statement) {
    statement.define(RESULTSET_MAPPER_ATTRIBUTE_NAME, objectMapper);
  }
  public void overrideStatementBinder(SQLStatement<?> statement) {
    statement.define(STATEMENT_BINDER_ATTRIBUTE_NAME, objectMapper);
  }

  public static ObjectMapper resolveResultSetMapper(StatementContext context) {
    Object override = context.getAttribute(RESULTSET_MAPPER_ATTRIBUTE_NAME);
    return override == null ? Rosetta.getMapper() : (ObjectMapper) override;
  }

  public static ObjectMapper resolveStatementBinder(StatementContext context) {
    Object override = context.getAttribute(STATEMENT_BINDER_ATTRIBUTE_NAME);
    return override == null ? Rosetta.getMapper() : (ObjectMapper) override;
  }
}
