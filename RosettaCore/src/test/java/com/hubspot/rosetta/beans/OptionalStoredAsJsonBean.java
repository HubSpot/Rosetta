package com.hubspot.rosetta.beans;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class OptionalStoredAsJsonBean {

  private final Optional<PolymorphicBean> bean;

  @JsonCreator
  public OptionalStoredAsJsonBean(@JsonProperty("bean") Optional<PolymorphicBean> bean) {
    this.bean = bean;
  }

  @StoredAsJson
  @JsonProperty("bean")
  public Optional<PolymorphicBean> getBean() {
    return bean;
  }
}
