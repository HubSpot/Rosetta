package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.hubspot.rosetta.annotations.RosettaDeserializationProperty;
import com.hubspot.rosetta.annotations.RosettaProperty;
import com.hubspot.rosetta.annotations.RosettaSerializationProperty;

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
      ObjectMapper mapper = (ObjectMapper) codec;

      mapper.setConfig(
        mapper
          .getDeserializationConfig()
          .with(
            new RosettaAnnotationIntrospectorPair(
              new RosettaAnnotationIntrospector(mapper),
              mapper.getDeserializationConfig().getAnnotationIntrospector(),
              RosettaDeserializationProperty.class,
              RosettaProperty.class
            )
          )
      );

      mapper.setConfig(
        mapper
          .getSerializationConfig()
          .with(
            new RosettaAnnotationIntrospectorPair(
              new RosettaAnnotationIntrospector(mapper),
              mapper.getSerializationConfig().getAnnotationIntrospector(),
              RosettaSerializationProperty.class,
              RosettaProperty.class
            )
          )
      );

      mapper.setSerializerProvider(new DefaultSerializerProvider.Impl());
      mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.setSerializationInclusion(Include.ALWAYS);
    }
  }
}
