package com.hubspot.rosetta.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.hubspot.rosetta.annotations.StoredAsJson;
import com.hubspot.rosetta.beans.StoredAsJsonListTypeInfoBean.ConcreteStoredAsJsonList;

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConcreteStoredAsJsonList.class)
})
public abstract class StoredAsJsonListTypeInfoBean {

  @StoredAsJson
  public abstract List<InnerBean> getInnerBeans();

  public static class ConcreteStoredAsJsonList extends StoredAsJsonListTypeInfoBean {
    private List<InnerBean> innerBeans;

    @Override
    public List<InnerBean> getInnerBeans() {
      return innerBeans;
    }

    public void setInnerBeans(List<InnerBean> innerBeans) {
      this.innerBeans = innerBeans;
    }
  }
}
