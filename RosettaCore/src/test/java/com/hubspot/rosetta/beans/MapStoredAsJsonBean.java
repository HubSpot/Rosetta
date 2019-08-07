package com.hubspot.rosetta.beans;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class MapStoredAsJsonBean {

  private final Map<String, PolymorphicBean> beans;

  @JsonCreator
  public MapStoredAsJsonBean(@JsonProperty("beans") Map<String, PolymorphicBean> beans) {
    this.beans = beans;
  }

  @StoredAsJson
  @JsonProperty("bean")
  public Map<String, PolymorphicBean> getBeans() {
    return beans;
  }
}
