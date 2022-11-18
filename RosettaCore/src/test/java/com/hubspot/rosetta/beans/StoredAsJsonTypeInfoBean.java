package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.hubspot.rosetta.beans.StoredAsJsonTypeInfoBean.ConcreteStoredAsJsonTypeInfo;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConcreteStoredAsJsonTypeInfo.class, name = "concrete")
})
@JsonPropertyOrder({"generalValue", "General","concreteValue","internal","type","concrete"})
public interface StoredAsJsonTypeInfoBean {

  String getType();
  String getGeneralValue();

  class ConcreteStoredAsJsonTypeInfo implements StoredAsJsonTypeInfoBean {
    private String generalValue;
    private String concreteValue;

    @Override
    public String getType() {
      return "concrete";
    }

    @Override
    public String getGeneralValue() {
      return generalValue;
    }

    public void setGeneralValue(String generalValue) {
      this.generalValue = generalValue;
    }

    public String getConcreteValue() {
      return concreteValue;
    }

    public void setConcreteValue(String concreteValue) {
      this.concreteValue = concreteValue;
    }
  }
}
