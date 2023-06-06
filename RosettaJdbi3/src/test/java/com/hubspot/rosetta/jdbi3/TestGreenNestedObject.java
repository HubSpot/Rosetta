package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestGreenNestedObject implements TestSubTypedNestedObject {

  private final int relatedId;
  private final long relaxLevel;
  private final String relaxSong;

  @JsonCreator
  public TestGreenNestedObject(@JsonProperty("relatedId") int relatedId,
                               @JsonProperty("relaxLevel") long relaxLevel,
                               @JsonProperty("relaxSong") String relaxSong) {
    this.relatedId = relatedId;
    this.relaxLevel = relaxLevel;
    this.relaxSong = relaxSong;
  }

  public int getRelatedId() {
    return relatedId;
  }

  public long getRelaxLevel() {
    return relaxLevel;
  }

  public String getRelaxSong() {
    return relaxSong;
  }

  @Override
  public String getColor() {
    return "GREEN";
  }
}
