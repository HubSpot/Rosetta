package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaNaming;
import com.hubspot.rosetta.annotations.RosettaProperty;
import com.hubspot.rosetta.annotations.RosettaValue;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class RosettaAnnotationIntrospector extends NopAnnotationIntrospector {
  private static final long serialVersionUID = 1L;

  @Override
  public Object findNamingStrategy(AnnotatedClass ac) {
    RosettaNaming ann = ac.getAnnotation(RosettaNaming.class);
    return ann == null ? null : ann.value();
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonSerializer<?> findSerializer(Annotated a) {
    StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
    if (storedAsJson == null) {
      return null;
    } else {
      Class<?> type = a.getRawType();
      return storedAsJson.binary() ? new StoredAsJsonBinarySerializer(type) : new StoredAsJsonSerializer(type);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonDeserializer<?> findDeserializer(Annotated a) {
    StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
    if (storedAsJson == null) {
      return null;
    } else {
      if (a instanceof AnnotatedMethod) {
        a = ((AnnotatedMethod) a).getParameter(0);
      }

      String empty = StoredAsJson.NULL.equals(storedAsJson.empty()) ? null : storedAsJson.empty();
      return new StoredAsJsonDeserializer(a.getRawType(), a.getGenericType(), empty);
    }
  }

  @Override
  public boolean hasAsValueAnnotation(AnnotatedMethod am) {
    return am.hasAnnotation(RosettaValue.class);
  }

  @Override
  public boolean hasCreatorAnnotation(Annotated a) {
    return a.hasAnnotation(RosettaCreator.class);
  }

  @Override
  public PropertyName findNameForSerialization(Annotated a) {
    PropertyName propertyName = findRosettaPropertyName(a);
    if (propertyName == null) {
      propertyName = super.findNameForSerialization(a);
    }
    return propertyName;
  }

  @Override
  public PropertyName findNameForDeserialization(Annotated a) {
    PropertyName propertyName = findRosettaPropertyName(a);
    if (propertyName == null) {
      propertyName = super.findNameForDeserialization(a);
    }
    return propertyName;
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  private PropertyName findRosettaPropertyName(Annotated a) {
    RosettaProperty ann = a.getAnnotation(RosettaProperty.class);
    if (ann != null) {
      return ann.value().isEmpty() ? PropertyName.USE_DEFAULT : new PropertyName(ann.value());
    }
    return null;
  }

}
