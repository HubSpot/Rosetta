package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.annotations.RosettaValue;

public class RosettaValueBean {

  private String stringProperty;

  @RosettaValue
  public String getStringProperty() {
    return stringProperty;
  }

  public void setStringProperty(String stringProperty) {
    this.stringProperty = stringProperty;
  }
}
