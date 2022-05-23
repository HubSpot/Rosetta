package com.hubspot.rosetta.immutables.beans;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.hubspot.immutables.utils.WireSafeEnum;

public class WireSafeBean {
  private WireSafeEnum<SimpleEnum> simple;
  private Optional<WireSafeEnum<SimpleEnum>> simpleMaybe;
  private List<WireSafeEnum<SimpleEnum>> simpleList;
  private Map<String, WireSafeEnum<SimpleEnum>> simpleMap;
  private WireSafeEnum<CustomEnum> custom;
  private Optional<WireSafeEnum<CustomEnum>> customMaybe;
  private List<WireSafeEnum<CustomEnum>> customList;
  private Map<String, WireSafeEnum<CustomEnum>> customMap;

  public WireSafeEnum<SimpleEnum> getSimple() {
    return simple;
  }

  public Optional<WireSafeEnum<SimpleEnum>> getSimpleMaybe() {
    return simpleMaybe;
  }

  public List<WireSafeEnum<SimpleEnum>> getSimpleList() {
    return simpleList;
  }

  public Map<String, WireSafeEnum<SimpleEnum>> getSimpleMap() {
    return simpleMap;
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


  public Map<String, WireSafeEnum<CustomEnum>> getCustomMap() {
    return customMap;
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

  public void setSimpleMap(Map<String, WireSafeEnum<SimpleEnum>> simpleMap) {
    this.simpleMap = simpleMap;
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

  public void setCustomMap(Map<String, WireSafeEnum<CustomEnum>> customMap) {
    this.customMap = customMap;
  }

  @Override
  public String toString() {
    return MoreObjects
        .toStringHelper(WireSafeBean.class)
        .add("simple", simple)
        .add("simpleMaybe", simpleMaybe)
        .add("simpleList", simpleList)
        .add("simpleMap", simpleMap)
        .add("custom", custom)
        .add("customMaybe", customMaybe)
        .add("customList", customList)
        .add("customMap", customMap)
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
        Objects.equal(simpleMap, bean.simpleMap) &&
        Objects.equal(custom, bean.custom) &&
        Objects.equal(customMaybe, bean.customMaybe) &&
        Objects.equal(customList, bean.customList) &&
        Objects.equal(customMap, bean.customMap);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(simple, simpleMaybe, simpleList, simpleMap, custom, customMaybe, customList, customMap);
  }
}
