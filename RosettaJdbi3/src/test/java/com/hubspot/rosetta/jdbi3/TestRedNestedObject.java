package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class TestRedNestedObject implements TestSubTypedNestedObject {

  private final int relatedId;
  private final long dangerLevel;

  public TestRedNestedObject(@JsonProperty("relatedId") int relatedId, @JsonProperty("dangerLevel") long dangerLevel) {
    this.relatedId = relatedId;
    this.dangerLevel = dangerLevel;
  }

  public int getRelatedId() {
    return relatedId;
  }

  public long getDangerLevel() {
    return dangerLevel;
  }

  @Override
  public String getColor() {
    return "RED";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestRedNestedObject that = (TestRedNestedObject) o;
    return relatedId == that.relatedId && dangerLevel == that.dangerLevel;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(relatedId, dangerLevel);
  }

  @Override
  public String toString() {
    return "TestRedNestedObject{" +
        "relatedId=" + relatedId +
        ", dangerLevel=" + dangerLevel +
        '}';
  }
}
