package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.CustomMappedValue;

public class GenderPojo implements CustomMappedValue {

  public static final GenderPojo MALE = new GenderPojo(0);
  public static final GenderPojo FEMALE = new GenderPojo(1);

  private int digit;

  public GenderPojo() {} // For Rosetta

  public GenderPojo(int digit) {
    this.digit = digit;
  }

  public int getDigit() {
    return digit;
  }

  public Object getMappedValue(Object defaultValue) {
    final int intValue = Integer.parseInt(String.class.cast(defaultValue));
    if (intValue == MALE.getDigit()) {
      return MALE;
    } else if (intValue == FEMALE.getDigit()) {
      return FEMALE;
    }
    return null;
  }
}
