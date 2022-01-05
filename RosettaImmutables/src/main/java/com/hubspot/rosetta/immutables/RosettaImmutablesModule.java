package com.hubspot.rosetta.immutables;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.hubspot.immutables.utils.WireSafeEnum;

public class RosettaImmutablesModule extends Module {

  @Override
  public String getModuleName() {
    return getClass().getName();
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  @Override
  public void setupModule(SetupContext context) {
    // use this mixin to override @JsonSerialize/@JsonDeserialize on WireSafeEnum
    context.setMixInAnnotations(WireSafeEnum.class, WireSafeEnumMixin.class);
  }
}
