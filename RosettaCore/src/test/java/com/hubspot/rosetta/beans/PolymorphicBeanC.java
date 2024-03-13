package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PolymorphicBeanC implements PolymorphicBean {

  private final String value;

  @JsonCreator
  public PolymorphicBeanC(@JsonProperty("value") String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
