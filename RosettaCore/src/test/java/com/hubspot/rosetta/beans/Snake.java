package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(LowerCaseWithUnderscoresStrategy.class)
public class Snake {

  private Boolean hasStripes;
  private Boolean isPoisonous;
  private String name;

  public Boolean getHasStripes() {
    return hasStripes;
  }

  public void setHasStripes(Boolean hasStripes) {
    this.hasStripes = hasStripes;
  }

  public Boolean getIsPoisonous() {
    return isPoisonous;
  }

  public void setIsPoisonous(Boolean isPoisonous) {
    this.isPoisonous = isPoisonous;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
