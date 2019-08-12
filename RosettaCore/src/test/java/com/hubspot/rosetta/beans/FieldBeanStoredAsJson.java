package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class FieldBeanStoredAsJson {

  @StoredAsJson
  private PolymorphicBean bean;

  @JsonCreator
  public FieldBeanStoredAsJson(@JsonProperty("bean") PolymorphicBean bean) {
    this.bean = bean;
  }
}
