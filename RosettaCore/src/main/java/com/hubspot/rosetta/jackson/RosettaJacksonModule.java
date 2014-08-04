package com.hubspot.rosetta.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SuppressWarnings("serial")
public class RosettaJacksonModule extends SimpleModule {

  @Override
  public String getModuleName() {
    return "RosettaModule";
  }

  @Override
  public Version version() {
    return new Version(1, 0, 0, null, null, null);
  }

  @Override
  public void setupModule(SetupContext context) {
    context.insertAnnotationIntrospector(new RosettaAnnotationIntrospector());
  }

}
