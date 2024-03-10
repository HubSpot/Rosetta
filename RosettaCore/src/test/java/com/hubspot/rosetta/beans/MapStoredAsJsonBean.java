package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;
import java.util.Map;

public class MapStoredAsJsonBean {

  private final Map<String, PolymorphicBean> beans;

  @JsonCreator
  public MapStoredAsJsonBean(@JsonProperty Map<String, PolymorphicBean> beans) {
    this.beans = beans;
  }

  @StoredAsJson
  @JsonProperty
  public Map<String, PolymorphicBean> getBeans() {
    return beans;
  }
}
