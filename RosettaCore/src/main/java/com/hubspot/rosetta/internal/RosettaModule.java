package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

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

    context.addBeanSerializerModifier(new StoredAsJsonBeanSerializerModifier());

    ObjectCodec codec = context.getOwner();
    if (codec instanceof ObjectMapper) {

      context.insertAnnotationIntrospector(new RosettaAnnotationIntrospector((ObjectMapper) codec));

      ObjectMapper mapper = (ObjectMapper) codec;
      mapper.setSerializerProvider(new DefaultSerializerProvider.Impl());
      mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
  }
}
