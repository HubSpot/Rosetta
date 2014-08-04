package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.Joinable;

public class Parent {

  private String adultName;
  @Joinable private Person child;

  public String getAdultName() {
    return adultName;
  }

  public void setAdultName(String name) {
    this.adultName = name;
  }

  public Person getChild() {
    return child;
  }

  public void setChild(Person child) {
    this.child = child;
  }

}
