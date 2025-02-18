package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RosettaNamingBean {

  private String stringProperty;

  public String getStringProperty() {
    return stringProperty;
  }

  public void setStringProperty(String stringProperty) {
    this.stringProperty = stringProperty;
  }
}
