package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;
import java.util.Set;

public class SetStoredAsJsonBean {

  private final Set<PolymorphicBean> beans;

  @JsonCreator
  public SetStoredAsJsonBean(@JsonProperty Set<PolymorphicBean> beans) {
    this.beans = beans;
  }

  @StoredAsJson
  @JsonProperty
  public Set<PolymorphicBean> getBeans() {
    return beans;
  }
}
