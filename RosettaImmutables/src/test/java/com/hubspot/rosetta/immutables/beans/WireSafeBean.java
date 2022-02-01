package com.hubspot.rosetta.immutables.beans;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.hubspot.immutables.utils.WireSafeEnum;

public class WireSafeBean {
    private WireSafeEnum<SimpleEnum> simple;
    private WireSafeEnum<CustomEnum> custom;

    public WireSafeEnum<SimpleEnum> getSimple() {
      return simple;
    }

  public WireSafeEnum<CustomEnum> getCustom() {
    return custom;
  }

  public void setSimple(WireSafeEnum<SimpleEnum> simple) {
    this.simple = simple;
  }

  public void setCustom(WireSafeEnum<CustomEnum> custom) {
    this.custom = custom;
  }

  @Override
  public String toString() {
    return MoreObjects
        .toStringHelper(WireSafeBean.class)
        .add("simple", simple)
        .add("custom", custom).toString();
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
        Objects.equal(custom, bean.custom);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(simple, custom);
  }
}
