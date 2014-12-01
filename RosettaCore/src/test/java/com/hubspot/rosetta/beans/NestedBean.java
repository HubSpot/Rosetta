package com.hubspot.rosetta.beans;

public class NestedBean {
  private InnerBean inner;

  public InnerBean getInner() {
    return inner;
  }

  public void setInner(InnerBean inner) {
    this.inner = inner;
  }
}
