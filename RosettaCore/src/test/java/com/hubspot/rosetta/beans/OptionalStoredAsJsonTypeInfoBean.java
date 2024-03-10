package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.common.base.Optional;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class OptionalStoredAsJsonTypeInfoBean {

  private final Optional<Polymorph> polymorphicField;

  public OptionalStoredAsJsonTypeInfoBean(Polymorph polymorphicField) {
    this.polymorphicField = Optional.of(polymorphicField);
  }

  @StoredAsJson
  @JsonProperty("polymorphicField")
  public Optional<Polymorph> getPolymorphicField() {
    return polymorphicField;
  }

  @JsonTypeInfo(use = Id.NAME)
  public interface Polymorph {
    String getName();

    default String getValue() {
      return "a value";
    }
  }
}
