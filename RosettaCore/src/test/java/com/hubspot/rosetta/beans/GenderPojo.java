package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.annotations.RosettaCreator;

public class GenderPojo {

  public static final GenderPojo MALE = new GenderPojo(0);
  public static final GenderPojo FEMALE = new GenderPojo(1);

  private int digit;

  @RosettaCreator
  public GenderPojo(int digit) {
    this.digit = digit;
  }

  public int getDigit() {
    return digit;
  }
}
