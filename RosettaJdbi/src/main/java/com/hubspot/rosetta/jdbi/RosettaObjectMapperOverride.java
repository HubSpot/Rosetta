package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.internal.RosettaModule;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.IDBI;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.StatementContext;

public class RosettaObjectMapperOverride {

  public static final String ATTRIBUTE_NAME = "_rosetta_object_mapper";

  private final ObjectMapper objectMapper;

  public RosettaObjectMapperOverride(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper.copy().registerModule(new RosettaModule());
  }

  public void override(DBI dbi) {
    override((IDBI) dbi);
  }

  public void override(IDBI dbi) {
    dbi.define(ATTRIBUTE_NAME, objectMapper);
  }

  public void override(Handle handle) {
    handle.define(ATTRIBUTE_NAME, objectMapper);
  }

  public void override(SQLStatement<?> statement) {
    statement.define(ATTRIBUTE_NAME, objectMapper);
  }

  public static ObjectMapper resolve(StatementContext context) {
    Object override = context.getAttribute(ATTRIBUTE_NAME);
    return override == null ? Rosetta.getMapper() : (ObjectMapper) override;
  }
}
