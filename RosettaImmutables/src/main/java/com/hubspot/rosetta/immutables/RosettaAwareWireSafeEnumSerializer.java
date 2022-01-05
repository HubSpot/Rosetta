package com.hubspot.rosetta.immutables;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hubspot.immutables.utils.WireSafeEnum;

public class RosettaAwareWireSafeEnumSerializer extends JsonSerializer<WireSafeEnum<?>> implements ContextualSerializer {
  private static final Map<Class<?>, JsonSerializer<WireSafeEnum<?>>> SERIALIZER_CACHE = new ConcurrentHashMap<>();

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
    JavaType contextualType = property.getType();
    if (contextualType != null && contextualType.hasRawClass(WireSafeEnum.class)) {
      JavaType[] typeParameters = contextualType.findTypeParameters(WireSafeEnum.class);
      if (typeParameters.length != 1) {
        prov.reportMappingProblem("Can not discover enum type for: " + contextualType);
      } else if (!typeParameters[0].isEnumType()) {
        prov.reportMappingProblem("Can not handle non-enum type: " + typeParameters[0].getRawClass());
      } else {
        return serializerFor(typeParameters[0].getRawClass());
      }
    } else {
      prov.reportMappingProblem("Can not handle contextualType: " + contextualType);
    }

    return null;
  }

  @Override
  public void serialize(WireSafeEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    serializers.reportMappingProblem("Expected createContextual to be called");
  }

  private static <T extends Enum<T>> JsonSerializer<?> serializerFor(Class<?> rawType) {
    return SERIALIZER_CACHE.computeIfAbsent(rawType, ignored -> {
      @SuppressWarnings("unchecked")
      Class<T> enumType = (Class<T>) rawType;
      return newDeserializer(enumType);
    });
  }

  private static <T extends Enum<T>> JsonSerializer<WireSafeEnum<?>> newDeserializer(Class<T> enumType) {
    return new JsonSerializer<WireSafeEnum<?>>() {
      @Override
      public void serialize(WireSafeEnum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Optional<? extends Enum<?>> enumValue = value.asEnum();
        if (enumValue.isPresent()) {
          serializers.defaultSerializeValue(enumValue.get(), gen);
        } else {
          serializers.reportMappingProblem(
              "Cannot serialize WireSafeEnum<%s> with unknown enum value: %s",
              enumType.getSimpleName(),
              value.asString());
        }
      }
    };
  }
}
