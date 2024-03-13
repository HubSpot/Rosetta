package com.hubspot.rosetta.jdbi3;

import com.google.common.collect.Maps;
import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaValue;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public enum TestEnum {
  A(1),
  B(2),
  C(3);

  private static final Map<Integer, TestEnum> LOOKUP = Maps.uniqueIndex(
    Arrays.asList(values()),
    TestEnum::getValue
  );

  private int value;

  TestEnum(int value) {
    this.value = value;
  }

  @RosettaValue
  public int getValue() {
    return value;
  }

  @RosettaCreator
  public static TestEnum getFor(int value) {
    return Objects.requireNonNull(LOOKUP.get(value));
  }
}
