package com.hubspot.rosetta.immutables.beans;

import com.hubspot.immutables.utils.WireSafeEnum;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class WireSafeEnumStoredAsJsonBean {

  @StoredAsJson
  private InnerWireSafeEnumBean inner;

  public InnerWireSafeEnumBean getInner() {
    return inner;
  }

  public void setInner(InnerWireSafeEnumBean inner) {
    this.inner = inner;
  }

  public static class InnerWireSafeEnumBean {

    private WireSafeEnum<CustomEnum> customEnum;
    private WireSafeEnum<SimpleEnum> simpleEnum;
    private String name;

    public WireSafeEnum<CustomEnum> getCustomEnum() {
      return customEnum;
    }

    public void setCustomEnum(WireSafeEnum<CustomEnum> customEnum) {
      this.customEnum = customEnum;
    }

    public WireSafeEnum<SimpleEnum> getSimpleEnum() {
      return simpleEnum;
    }

    public void setSimpleEnum(WireSafeEnum<SimpleEnum> simpleEnum) {
      this.simpleEnum = simpleEnum;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
