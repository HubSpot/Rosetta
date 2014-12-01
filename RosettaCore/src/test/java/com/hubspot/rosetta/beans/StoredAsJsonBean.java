package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.annotations.StoredAsJson;

public class StoredAsJsonBean {

  @StoredAsJson
  private InnerBean annotatedField;
  private InnerBean annotatedGetter;
  private InnerBean annotatedSetter;

  @StoredAsJson(empty = "{\"stringProperty\":\"value\"}")
  private InnerBean annotatedFieldWithDefault;
  private InnerBean annotatedGetterWithDefault;
  private InnerBean annotatedSetterWithDefault;

  public InnerBean getAnnotatedField() {
    return annotatedField;
  }

  public void setAnnotatedField(InnerBean annotatedField) {
    this.annotatedField = annotatedField;
  }

  @StoredAsJson
  public InnerBean getAnnotatedGetter() {
    return annotatedGetter;
  }

  public void setAnnotatedGetter(InnerBean annotatedGetter) {
    this.annotatedGetter = annotatedGetter;
  }

  public InnerBean getAnnotatedSetter() {
    return annotatedSetter;
  }

  @StoredAsJson
  public void setAnnotatedSetter(InnerBean annotatedSetter) {
    this.annotatedSetter = annotatedSetter;
  }

  public InnerBean getAnnotatedFieldWithDefault() {
    return annotatedFieldWithDefault;
  }

  public void setAnnotatedFieldWithDefault(InnerBean annotatedFieldWithDefault) {
    this.annotatedFieldWithDefault = annotatedFieldWithDefault;
  }

  @StoredAsJson(empty = "{\"stringProperty\":\"value\"}")
  public InnerBean getAnnotatedGetterWithDefault() {
    return annotatedGetterWithDefault;
  }

  public void setAnnotatedGetterWithDefault(InnerBean annotatedGetterWithDefault) {
    this.annotatedGetterWithDefault = annotatedGetterWithDefault;
  }

  public InnerBean getAnnotatedSetterWithDefault() {
    return annotatedSetterWithDefault;
  }

  @StoredAsJson(empty = "{\"stringProperty\":\"value\"}")
  public void setAnnotatedSetterWithDefault(InnerBean annotatedSetterWithDefault) {
    this.annotatedSetterWithDefault = annotatedSetterWithDefault;
  }
}
