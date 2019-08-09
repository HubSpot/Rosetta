package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class InaccessibleStoredAsJsonBean {
  // No getter or @JsonProperty
  @StoredAsJson
  private final FieldBean fieldBean;

  @JsonCreator
  public InaccessibleStoredAsJsonBean(
      @JsonProperty("fieldBean") FieldBean fieldBean
  ) {
    this.fieldBean = fieldBean;
  }

  public static class FieldBean {
    @JsonProperty
    private final int id;

    @JsonCreator
    public FieldBean(@JsonProperty("id") int id) {
      this.id = id;
    }
  }
}
