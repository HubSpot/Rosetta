package com.hubspot.rosetta.immutables.beans;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaValue;

public enum CustomEnum {
  ONE(1),
  TWO(2),
  ;

  private static final Map<Integer, CustomEnum> LOOKUP =
      Maps.uniqueIndex(
          Arrays.asList(CustomEnum.values()),
          CustomEnum::getValue);

  private final int value;

  CustomEnum(int value) {
    this.value = value;
  }

  @RosettaValue
  public int getValue() {
    return value;
  }

  @RosettaCreator
  public static CustomEnum from(int value) {
    return Optional.ofNullable(LOOKUP.get(value))
        .orElseThrow(() -> new IllegalArgumentException("No enum for value: " + value));
  }
}
