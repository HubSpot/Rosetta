package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.annotations.StoredAsJson;

public class PolymorphicStoredAsJsonBean {

  @StoredAsJson
  private PolymorphicBean annotatedField;

  @StoredAsJson
  public void setAnnotatedField(PolymorphicBean annotatedField) {
    this.annotatedField = annotatedField;
  }

  public PolymorphicBean getAnnotatedField() {
    return annotatedField;
  }
}
