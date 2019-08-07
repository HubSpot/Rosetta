package com.hubspot.rosetta.beans;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class SetStoredAsJsonBean {

  private final Set<PolymorphicBean> beans;

  @JsonCreator
  public SetStoredAsJsonBean(@JsonProperty("beans") Set<PolymorphicBean> beans) {
    this.beans = beans;
  }

  @StoredAsJson
  @JsonProperty("bean")
  public Set<PolymorphicBean> getBeans() {
    return beans;
  }
}
