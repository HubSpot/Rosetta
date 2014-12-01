package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(LowerCaseWithUnderscoresStrategy.class)
public class RosettaNamingBean {
  private String stringProperty;

  public String getStringProperty() {
    return stringProperty;
  }

  public void setStringProperty(String stringProperty) {
    this.stringProperty = stringProperty;
  }
}
