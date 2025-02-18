package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SingleValueWrapperBean {

  private final String value;

  private SingleValueWrapperBean(String value) {
    this.value = value;
  }

  @JsonValue
  public String get() {
    return value;
  }

  @JsonCreator
  public static SingleValueWrapperBean of(String value) {
    return new SingleValueWrapperBean(value);
  }
}
