package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.annotations.StoredAsJson;

public class StoredAsJsonBean {

  @StoredAsJson
  private Inner annotatedField;
  private Inner annotatedGetter;
  private Inner annotatedSetter;

  @StoredAsJson(empty = "{\"stringProperty\":\"value\"}")
  private Inner annotatedFieldWithDefault;
  private Inner annotatedGetterWithDefault;
  private Inner annotatedSetterWithDefault;

  public Inner getAnnotatedField() {
    return annotatedField;
  }

  public void setAnnotatedField(Inner annotatedField) {
    this.annotatedField = annotatedField;
  }

  @StoredAsJson
  public Inner getAnnotatedGetter() {
    return annotatedGetter;
  }

  public void setAnnotatedGetter(Inner annotatedGetter) {
    this.annotatedGetter = annotatedGetter;
  }

  public Inner getAnnotatedSetter() {
    return annotatedSetter;
  }

  @StoredAsJson
  public void setAnnotatedSetter(Inner annotatedSetter) {
    this.annotatedSetter = annotatedSetter;
  }

  public Inner getAnnotatedFieldWithDefault() {
    return annotatedFieldWithDefault;
  }

  public void setAnnotatedFieldWithDefault(Inner annotatedFieldWithDefault) {
    this.annotatedFieldWithDefault = annotatedFieldWithDefault;
  }

  @StoredAsJson(empty = "{\"stringProperty\":\"value\"}")
  public Inner getAnnotatedGetterWithDefault() {
    return annotatedGetterWithDefault;
  }

  public void setAnnotatedGetterWithDefault(Inner annotatedGetterWithDefault) {
    this.annotatedGetterWithDefault = annotatedGetterWithDefault;
  }

  public Inner getAnnotatedSetterWithDefault() {
    return annotatedSetterWithDefault;
  }

  @StoredAsJson(empty = "{\"stringProperty\":\"value\"}")
  public void setAnnotatedSetterWithDefault(Inner annotatedSetterWithDefault) {
    this.annotatedSetterWithDefault = annotatedSetterWithDefault;
  }

  public static class Inner {
    private String stringProperty;

    public String getStringProperty() {
      return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
      this.stringProperty = stringProperty;
    }
  }
}
