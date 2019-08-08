package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "beanType")
@JsonSubTypes({
    @Type(value = PolymorphicBeanA.class, name = "A"),
    @Type(value = PolymorphicBeanB.class, name = "B"),
    @Type(value = PolymorphicBeanC.class, name = "C"),
    @Type(value = PolymorphicBeanASubTypeA.class, name = "AsA")
})
public interface PolymorphicBean {
  String getValue();
}
