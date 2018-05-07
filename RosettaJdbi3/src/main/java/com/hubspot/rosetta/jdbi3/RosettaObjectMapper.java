package com.hubspot.rosetta.jdbi3;

import org.jdbi.v3.core.config.JdbiConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;

public class RosettaObjectMapper implements JdbiConfig<RosettaObjectMapper> {
  private ObjectMapper objectMapper;

  public RosettaObjectMapper() {
    this(Rosetta.getMapper());
  }

  private RosettaObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public RosettaObjectMapper createCopy() {
    return new RosettaObjectMapper(objectMapper);
  }
}
