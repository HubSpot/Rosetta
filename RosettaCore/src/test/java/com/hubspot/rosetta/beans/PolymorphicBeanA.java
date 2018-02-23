package com.hubspot.rosetta.beans;

public class PolymorphicBeanA implements PolymorphicBean {

  @Override
  public String getValue() {
    return "a value";
  }

  public String getName() {
    return "a";
  }
}
