package com.hubspot.rosetta.beans;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public interface OptionalStoredAsJsonBeanIF {

  @StoredAsJson
  @JsonProperty
  Optional<PolymorphicBean> getBean();

  class OptionalStoredAsJsonBean implements OptionalStoredAsJsonBeanIF {

    private final PolymorphicBean bean;

    @JsonCreator
    public OptionalStoredAsJsonBean(@JsonProperty Optional<PolymorphicBean> bean) {
      this.bean = bean.orElse(null);
    }

    @Override
    @StoredAsJson
    @JsonProperty
    public Optional<PolymorphicBean> getBean() {
      return Optional.ofNullable(bean);
    }
  }
}

