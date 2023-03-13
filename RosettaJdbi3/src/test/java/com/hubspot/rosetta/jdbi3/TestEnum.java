package com.hubspot.rosetta.jdbi3;

import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaValue;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TestEnum {
  A(1),
  B(2),
  C(3),
  ;

  private static final Map<Integer, TestEnum> LOOKUP = Collections.unmodifiableMap(
      Arrays.stream(TestEnum.values())
          .collect(Collectors.toMap(
              TestEnum::getValue,
              Function.identity())));

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
