package com.hubspot.rosetta.immutables;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.hubspot.immutables.utils.WireSafeEnum;

public class RosettaAwareWireSafeEnumDeserializer extends JsonDeserializer<WireSafeEnum<?>> implements ContextualDeserializer {
  private static final Map<Class<?>, JsonDeserializer<WireSafeEnum<?>>> DESERIALIZER_CACHE = new ConcurrentHashMap<>();

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
    Class<?> enumType = extractEnumType(ctxt);
    return deserializerFor(enumType);
  }

  @Override
  public WireSafeEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    throw ctxt.mappingException("Expected createContextual to be called");
  }

  @SuppressWarnings("unchecked")
  private static <T extends Enum<T>> JsonDeserializer<?> deserializerFor(Class<?> rawType) {
    return DESERIALIZER_CACHE.computeIfAbsent(rawType, ignored -> newDeserializer((Class<T>) rawType));
  }

  private static <T extends Enum<T>> JsonDeserializer<WireSafeEnum<?>> newDeserializer(Class<T> enumType) {
    return new JsonDeserializer<WireSafeEnum<?>>() {

      @Override
      public WireSafeEnum<T> deserialize(JsonParser p, DeserializationContext ctxt) {
        try {
          return WireSafeEnum.of(p.getCodec().readValue(p, enumType));
        } catch (IOException e) {
          throw new IllegalStateException("Invalid value for enum type: " + enumType.getTypeName(), e);
        }
      }
    };
  }

  private static Class<?> extractEnumType(DeserializationContext context) throws JsonMappingException {
    JavaType javaType = context.getContextualType();

    if (!WireSafeEnum.class.equals(javaType.getRawClass())) {
      context.reportMappingException("Expected contextual type to be WireSafeEnum, got: " + javaType);
    } else {
      JavaType typeParameter = javaType.findTypeParameters(WireSafeEnum.class)[0];
      if (!typeParameter.isEnumType()) {
        context.reportMappingException("Can not handle non-enum type: " + typeParameter);
      } else {
        return typeParameter.getRawClass();
      }
    }

    throw new IllegalStateException(); // should never get here
  }
}
