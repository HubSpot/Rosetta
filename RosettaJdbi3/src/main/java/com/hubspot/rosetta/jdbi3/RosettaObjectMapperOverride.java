package com.hubspot.rosetta.jdbi3;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatement;
import org.jdbi.v3.core.statement.StatementContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.internal.RosettaModule;

public class RosettaObjectMapperOverride {
  public static final String ATTRIBUTE_NAME = "_rosetta_object_mapper";

  private final ObjectMapper objectMapper;

  public RosettaObjectMapperOverride(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper.copy().registerModule(new RosettaModule());
  }

  public void override(Jdbi jdbi) {
    jdbi.define(ATTRIBUTE_NAME, objectMapper);
  }

  public void override(Handle handle) {
    handle.define(ATTRIBUTE_NAME, objectMapper);
  }

  public void override(SqlStatement<?> statement) {
    statement.define(ATTRIBUTE_NAME, objectMapper);
  }

  public static ObjectMapper resolve(StatementContext context) {
    Object override = context.getAttribute(ATTRIBUTE_NAME);
    return override == null ? Rosetta.getMapper() : (ObjectMapper) override;
  }
}
