package com.hubspot.rosetta.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public interface ListStoredAsJsonBeanIF {

  @StoredAsJson
  @JsonProperty
  List<PolymorphicBean> getBeans();

  class ListStoredAsJsonBean implements ListStoredAsJsonBeanIF {

    private final List<PolymorphicBean> beans;

    @JsonCreator
    public ListStoredAsJsonBean(@JsonProperty List<PolymorphicBean> beans) {
      this.beans = beans;
    }

    @Override
    @StoredAsJson
    @JsonProperty
    public List<PolymorphicBean> getBeans() {
      return beans;
    }
  }
}
