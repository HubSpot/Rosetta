package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.util.List;
import java.util.Objects;

public class TestListObject {
  private int id;
  private TestEnum value;
  private List<String> stringValues;
  private TestObject objectValue;

  public TestListObject(int id, TestEnum value) {
    this (id, value, null);
  }

  public TestListObject(int id, TestEnum value, List<String> stringValues) {
    this (id, value, stringValues, null);
  }

  @JsonCreator
  public TestListObject(
      @JsonProperty("id") int id,
      @JsonProperty("value") TestEnum value,
      @JsonProperty("stringValues") List<String> stringValues,
      @JsonProperty("objectValue") TestObject objectValue) {
    this.id = id;
    this.value = value;
    this.stringValues = stringValues;
    this.objectValue = objectValue;
  }

  public int getId() {
    return id;
  }

  public TestEnum getValue() {
    return value;
  }

  public List<String> getStringValues() {
    return stringValues;
  }

  public TestObject getObjectValue() {
    return objectValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TestListObject that = (TestListObject) o;
    return id == that.id &&
        Objects.equals(value, that.value) &&
        Objects.equals(stringValues, that.stringValues) &&
        Objects.equals(objectValue, that.objectValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, value, stringValues, objectValue);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("value", value)
        .add("stringValues", stringValues)
        .add("objectValue", objectValue)
        .toString();
  }
}
