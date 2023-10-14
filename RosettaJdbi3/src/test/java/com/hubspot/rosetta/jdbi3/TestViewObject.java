package com.hubspot.rosetta.jdbi3;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.hubspot.rosetta.annotations.NestedOptional;

public class TestViewObject {

  private final int id;
  private final String name;

  @NestedOptional
  private final Optional<TestRelatedObject> related;

  @JsonCreator
  public TestViewObject(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("related") Optional<TestRelatedObject> related) {
    this.id = id;
    this.name = name;
    this.related = Preconditions.checkNotNull(related);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Optional<TestRelatedObject> getRelated() {
    return related;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestViewObject that = (TestViewObject) o;
    return id == that.id && Objects.equal(name, that.name) && Objects.equal(related, that.related);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, related);
  }

  @Override
  public String toString() {
    return "TestViewObject{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", related=" + related +
        '}';
  }
}
