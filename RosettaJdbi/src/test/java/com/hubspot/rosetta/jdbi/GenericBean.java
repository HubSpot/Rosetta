package com.hubspot.rosetta.jdbi;

public class GenericBean<T> {
  private final T value;

  public GenericBean(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }
}
