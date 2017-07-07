package com.hubspot.rosetta.internal;

import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaDeserialize;
import com.hubspot.rosetta.annotations.RosettaNaming;
import com.hubspot.rosetta.annotations.RosettaProperty;
import com.hubspot.rosetta.annotations.RosettaSerialize;
import com.hubspot.rosetta.annotations.RosettaValue;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class RosettaAnnotationIntrospector extends NopAnnotationIntrospector {
  private static final long serialVersionUID = 1L;

  private final ObjectMapper objectMapper;

  public RosettaAnnotationIntrospector(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Object findNamingStrategy(AnnotatedClass ac) {
    RosettaNaming ann = ac.getAnnotation(RosettaNaming.class);
    return ann == null ? null : ann.value();
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonSerializer<?> findSerializer(Annotated a) {
    StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
    if (storedAsJson != null) {
      Class<?> type = a.getRawType();
      return storedAsJson.binary() ? new StoredAsJsonBinarySerializer(type) : new StoredAsJsonSerializer(type);
    }

    RosettaSerialize rosettaSerialize = a.getAnnotation(RosettaSerialize.class);
    if (rosettaSerialize != null) {
      Class<? extends JsonSerializer> klass = rosettaSerialize.using();
      if (klass != JsonSerializer.None.class) {
        return ClassUtil.createInstance(
            klass,
            objectMapper.getSerializationConfig().canOverrideAccessModifiers());
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonDeserializer<?> findDeserializer(Annotated a) {
    StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
    if (storedAsJson != null) {
      if (a instanceof AnnotatedMethod) {
        a = ((AnnotatedMethod) a).getParameter(0);
      }

      String empty = StoredAsJson.NULL.equals(storedAsJson.empty()) ? "null" : storedAsJson.empty();
      return new StoredAsJsonDeserializer(a.getRawType(), getType(a), empty, objectMapper);
    }

    RosettaDeserialize rosettaDeserialize = a.getAnnotation(RosettaDeserialize.class);
    if (rosettaDeserialize != null) {
      Class<? extends JsonDeserializer> klass = rosettaDeserialize.using();
      if (klass != JsonDeserializer.None.class) {
        return ClassUtil.createInstance(
            klass,
            objectMapper.getDeserializationConfig().canOverrideAccessModifiers());
      }
    }

    return null;
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
  public Include findSerializationInclusion(Annotated a, Include defValue) {
    return Include.ALWAYS;
  }

  @Override
  public Include findSerializationInclusionForContent(Annotated a, Include defValue) {
    return Include.ALWAYS;
  }

  @Override
  public Value findPropertyInclusion(Annotated a) {
    return Value.construct(Include.ALWAYS, Include.ALWAYS);
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

  private Type getType(Annotated a) {
    try {
      // Jackson 2.7+
      return a.getType();
    } catch (Throwable t) {
      return a.getGenericType();
    }
  }
}
