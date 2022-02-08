package com.hubspot.rosetta.immutables.beans;

import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.hubspot.immutables.utils.WireSafeEnum;

public class WireSafeBean {
  private WireSafeEnum<SimpleEnum> simple;
  private Optional<WireSafeEnum<SimpleEnum>> simpleMaybe;
  private WireSafeEnum<CustomEnum> custom;
  private Optional<WireSafeEnum<CustomEnum>> customMaybe;

  public WireSafeEnum<SimpleEnum> getSimple() {
      return simple;
    }

  public Optional<WireSafeEnum<SimpleEnum>> getSimpleMaybe() {
    return simpleMaybe;
  }

  public WireSafeEnum<CustomEnum> getCustom() {
    return custom;
  }

  public Optional<WireSafeEnum<CustomEnum>> getCustomMaybe() {
    return customMaybe;
  }

  public void setSimple(WireSafeEnum<SimpleEnum> simple) {
    this.simple = simple;
  }

  public void setSimpleMaybe(Optional<WireSafeEnum<SimpleEnum>> simpleMaybe) {
    this.simpleMaybe = simpleMaybe;
  }

  public void setCustom(WireSafeEnum<CustomEnum> custom) {
    this.custom = custom;
  }

  public void setCustomMaybe(Optional<WireSafeEnum<CustomEnum>> customMaybe) {
    this.customMaybe = customMaybe;
  }

  @Override
  public String toString() {
    return MoreObjects
        .toStringHelper(WireSafeBean.class)
        .add("simple", simple)
        .add("simpleMaybe", simpleMaybe)
        .add("custom", custom)
        .add("customMaybe", customMaybe)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WireSafeBean bean = (WireSafeBean) o;
    return Objects.equal(simple, bean.simple) &&
        Objects.equal(simpleMaybe, bean.simpleMaybe) &&
        Objects.equal(custom, bean.custom) &&
        Objects.equal(customMaybe, bean.customMaybe);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(simple, simpleMaybe, custom, customMaybe);
  }
}
