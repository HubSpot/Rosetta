package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
  MALE(0), FEMALE(1);

  private final int digit;

  @JsonCreator
  private Gender(int digit) {
    this.digit = digit;
  }

  @JsonValue
  public final int getDigit() {
    return digit;
  }
}
