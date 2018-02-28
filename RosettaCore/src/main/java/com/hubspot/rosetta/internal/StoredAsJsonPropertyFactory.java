package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public class StoredAsJsonPropertyFactory {

  public static SettableBeanProperty createFrom(SettableBeanProperty property, DeserializationConfig config, BeanDescription beanDesc, BeanDeserializerBuilder builder) {
    return property;
//    return new StoredAsJsonMethodProperty(
//        getPropertyDefinition(property, beanDesc),
//        property.getType(),
//        property.getValueTypeDeserializer(),
//        beanDesc.getClassAnnotations(),
//        (AnnotatedMethod) property.getMember());
  }

  private static BeanPropertyDefinition getPropertyDefinition(SettableBeanProperty property, BeanDescription description) {
    for (BeanPropertyDefinition def : description.findProperties()) {
      if (def.getFullName().equals(property.getFullName())) {
        return def;
      }
    }

    throw new IllegalArgumentException("Could not find suitable property definition for property " + property.getName());
  }
}
