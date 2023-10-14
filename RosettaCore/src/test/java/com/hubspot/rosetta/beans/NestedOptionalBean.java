package com.hubspot.rosetta.beans;

import java.util.Optional;

import com.hubspot.rosetta.annotations.NestedOptional;

public class NestedOptionalBean {

  @NestedOptional
  private Optional<InnerBean> firstNestedOptional;

  @NestedOptional
  private Optional<MoreFieldsInnerFieldBean> secondNestedOptional;

  public Optional<InnerBean> getFirstNestedOptional() {
    return firstNestedOptional;
  }

  public void setFirstNestedOptional(Optional<InnerBean> firstNestedOptional) {
    this.firstNestedOptional = firstNestedOptional;
  }

  public Optional<MoreFieldsInnerFieldBean> getSecondNestedOptional() {
    return secondNestedOptional;
  }

  public void setSecondNestedOptional(Optional<MoreFieldsInnerFieldBean> secondNestedOptional) {
    this.secondNestedOptional = secondNestedOptional;
  }
}
