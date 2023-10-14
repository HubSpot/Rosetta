package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class TestRelatedObject {
  private final int id;
  private final int relatedId;
  private final String otherName;
  private final long score;

  @JsonCreator
  public TestRelatedObject(@JsonProperty("id") int id, @JsonProperty("relatedId") int relatedId, @JsonProperty("otherName") String otherName, @JsonProperty("score") long score) {
    this.id = id;
    this.relatedId = relatedId;
    this.otherName = otherName;
    this.score = score;
  }

  public int getId() {
    return id;
  }

  public int getRelatedId() {
    return relatedId;
  }

  public String getOtherName() {
    return otherName;
  }

  public long getScore() {
    return score;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestRelatedObject that = (TestRelatedObject) o;
    return id == that.id && relatedId == that.relatedId && score == that.score && Objects.equal(otherName, that.otherName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, relatedId, otherName, score);
  }

  @Override
  public String toString() {
    return "TestRelatedObject{" +
        "id=" + id +
        ", relatedId=" + relatedId +
        ", name='" + otherName + '\'' +
        ", score=" + score +
        '}';
  }
}
