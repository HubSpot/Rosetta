package com.hubspot.rosetta.jdbi3;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.hubspot.rosetta.annotations.NestedOptional;

public class TestSubTypedViewObject {
  private final int id;
  private final String name;

  @NestedOptional
  private final Optional<TestSubTypedNestedObject> related;

  @JsonCreator
  public TestSubTypedViewObject(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("related") Optional<TestSubTypedNestedObject> related) {
    this.id = id;
    this.name = name;
    this.related = related;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Optional<TestSubTypedNestedObject> getRelated() {
    return related;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestSubTypedViewObject that = (TestSubTypedViewObject) o;
    return id == that.id && Objects.equal(name, that.name) && Objects.equal(related, that.related);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, related);
  }

  @Override
  public String toString() {
    return "TestSubTypedViewObject{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", related=" + related +
        '}';
  }
}
