package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubspot.rosetta.util.NullSerializer;

@JsonSerialize(using = NullSerializer.class)
public class NullPolymorphicBean implements PolymorphicBean {

  @Override
  public String getValue() {
    return null;
  }
}
