package com.hubspot.rosetta.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class ListStoredAsJsonBean {

  private final List<PolymorphicBean> beans;

  @JsonCreator
  public ListStoredAsJsonBean(@JsonProperty("beans") List<PolymorphicBean> beans) {
    this.beans = beans;
  }

  @StoredAsJson
  @JsonProperty("bean")
  public List<PolymorphicBean> getBeans() {
    return beans;
  }
}
