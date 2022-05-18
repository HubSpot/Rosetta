package com.hubspot.rosetta.immutables.beans;

import java.util.List;
import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.hubspot.immutables.utils.WireSafeEnum;

public class WireSafeBean {
  private WireSafeEnum<SimpleEnum> simple;
  private Optional<WireSafeEnum<SimpleEnum>> simpleMaybe;
  private List<WireSafeEnum<SimpleEnum>> simpleList;
  private WireSafeEnum<CustomEnum> custom;
  private Optional<WireSafeEnum<CustomEnum>> customMaybe;
  private List<WireSafeEnum<CustomEnum>> customList;

  public WireSafeEnum<SimpleEnum> getSimple() {
      return simple;
    }

  public Optional<WireSafeEnum<SimpleEnum>> getSimpleMaybe() {
    return simpleMaybe;
  }

  public List<WireSafeEnum<SimpleEnum>> getSimpleList() {
    return simpleList;
  }

  public WireSafeEnum<CustomEnum> getCustom() {
    return custom;
  }

  public Optional<WireSafeEnum<CustomEnum>> getCustomMaybe() {
    return customMaybe;
  }

  public List<WireSafeEnum<CustomEnum>> getCustomList() {
    return customList;
  }

  public void setSimple(WireSafeEnum<SimpleEnum> simple) {
    this.simple = simple;
  }

  public void setSimpleMaybe(Optional<WireSafeEnum<SimpleEnum>> simpleMaybe) {
    this.simpleMaybe = simpleMaybe;
  }

  public void setSimpleList(List<WireSafeEnum<SimpleEnum>> simpleList) {
    this.simpleList = simpleList;
  }

  public void setCustom(WireSafeEnum<CustomEnum> custom) {
    this.custom = custom;
  }

  public void setCustomMaybe(Optional<WireSafeEnum<CustomEnum>> customMaybe) {
    this.customMaybe = customMaybe;
  }

  public void setCustomList(List<WireSafeEnum<CustomEnum>> customList) {
    this.customList = customList;
  }

  @Override
  public String toString() {
    return MoreObjects
        .toStringHelper(WireSafeBean.class)
        .add("simple", simple)
        .add("simpleMaybe", simpleMaybe)
        .add("simpleList", simpleList)
        .add("custom", custom)
        .add("customMaybe", customMaybe)
        .add("customList", customList)
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
        Objects.equal(simpleList, bean.simpleList) &&
        Objects.equal(custom, bean.custom) &&
        Objects.equal(customMaybe, bean.customMaybe) &&
        Objects.equal(customList, bean.customList);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(simple, simpleMaybe, simpleList, custom, customMaybe, customList);
  }
}
