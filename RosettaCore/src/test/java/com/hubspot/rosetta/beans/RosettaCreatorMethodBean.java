package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.RosettaCreator;

public class RosettaCreatorMethodBean {

  private final String stringProperty;

  private RosettaCreatorMethodBean(String stringProperty) {
    this.stringProperty = stringProperty;
  }

  @RosettaCreator
  public static RosettaCreatorMethodBean fromString(
    @JsonProperty("stringProperty") String stringProperty
  ) {
    return new RosettaCreatorMethodBean(stringProperty);
  }

  public String getStringProperty() {
    return stringProperty;
  }
}
