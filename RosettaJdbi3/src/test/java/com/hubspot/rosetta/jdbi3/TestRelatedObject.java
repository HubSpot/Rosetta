package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class TestRelatedObject {
  private final int id;
  private final int relatedId;
  private final String name;
  private final long score;

  @JsonCreator
  public TestRelatedObject(@JsonProperty("id") int id, @JsonProperty("relatedId") int relatedId, @JsonProperty("name") String name, @JsonProperty("score") long score) {
    this.id = id;
    this.relatedId = relatedId;
    this.name = name;
    this.score = score;
  }

  public int getId() {
    return id;
  }

  public int getRelatedId() {
    return relatedId;
  }

  public String getName() {
    return name;
  }

  public long getScore() {
    return score;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestRelatedObject that = (TestRelatedObject) o;
    return id == that.id && relatedId == that.relatedId && score == that.score && Objects.equal(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, relatedId, name, score);
  }

  @Override
  public String toString() {
    return "TestRelatedObject{" +
        "id=" + id +
        ", relatedId=" + relatedId +
        ", name='" + name + '\'' +
        ", score=" + score +
        '}';
  }
}
