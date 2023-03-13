package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class TestListObject {
  private int id;
  private TestEnum value;

  @JsonCreator
  public TestListObject(@JsonProperty("id") int id, @JsonProperty("value") TestEnum value) {
    this.id = id;
    this.value = value;
  }

  public int getId() {
    return id;
  }

  public TestEnum getValue() {
    return value;
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
    return id == that.id && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, value);
  }

  @Override
  public String toString() {
    return String.format("%s{id=%s, value=%s}", getClass().getSimpleName(), id, value);
  }
}
