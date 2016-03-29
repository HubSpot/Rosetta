package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
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

  @StoredAsJson
  private Optional<InnerBean> optionalField;
  private Optional<InnerBean> optionalGetter;
  private Optional<InnerBean> optionalSetter;

  @StoredAsJson(binary = true)
  private InnerBean binaryField;
  @StoredAsJson(binary = true, empty = "{\"stringProperty\":\"value\"}")
  private InnerBean binaryFieldWithDefault;

  @StoredAsJson
  private JsonNode jsonNodeField;

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

  public Optional<InnerBean> getOptionalField() {
    return optionalField;
  }

  public void setOptionalField(Optional<InnerBean> optionalField) {
    this.optionalField = optionalField;
  }

  @StoredAsJson
  public Optional<InnerBean> getOptionalGetter() {
    return optionalGetter;
  }

  public void setOptionalGetter(Optional<InnerBean> optionalGetter) {
    this.optionalGetter = optionalGetter;
  }

  public Optional<InnerBean> getOptionalSetter() {
    return optionalSetter;
  }

  @StoredAsJson
  public void setOptionalSetter(Optional<InnerBean> optionalSetter) {
    this.optionalSetter = optionalSetter;
  }

  public InnerBean getBinaryField() {
    return binaryField;
  }

  public StoredAsJsonBean setBinaryField(InnerBean binaryField) {
    this.binaryField = binaryField;
    return this;
  }

  public InnerBean getBinaryFieldWithDefault() {
    return binaryFieldWithDefault;
  }

  public StoredAsJsonBean setBinaryFieldWithDefault(InnerBean binaryFieldWithDefault) {
    this.binaryFieldWithDefault = binaryFieldWithDefault;
    return this;
  }

  public JsonNode getJsonNodeField() {
    return jsonNodeField;
  }

  public StoredAsJsonBean setJsonNodeField(JsonNode jsonNodeField) {
    this.jsonNodeField = jsonNodeField;
    return this;
  }
}
