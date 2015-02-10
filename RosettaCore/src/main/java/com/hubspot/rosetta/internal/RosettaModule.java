package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

@SuppressWarnings("serial")
public class RosettaModule extends Module {

  @Override
  public String getModuleName() {
    return "RosettaModule";
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  @Override
  public void setupModule(SetupContext context) {
    context.insertAnnotationIntrospector(new RosettaAnnotationIntrospector());
    context.addBeanSerializerModifier(new StoredAsJsonBeanSerializerModifier());
  }
}
