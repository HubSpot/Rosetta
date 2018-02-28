package com.hubspot.rosetta.beans;

public class PolymorphicBeanB implements PolymorphicBean {

  @Override
  public String getValue() {
    return "b value";
  }

  public String getName() {
    return "b";
  }
}
