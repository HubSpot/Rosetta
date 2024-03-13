package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.RosettaCreator;

public class RosettaCreatorConstructorBean {

  private final String stringProperty;

  @RosettaCreator
  public RosettaCreatorConstructorBean(
    @JsonProperty("stringProperty") String stringProperty
  ) {
    this.stringProperty = stringProperty;
  }

  public String getStringProperty() {
    return stringProperty;
  }
}
