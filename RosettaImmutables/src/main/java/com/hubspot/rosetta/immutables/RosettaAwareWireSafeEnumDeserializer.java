package com.hubspot.rosetta.immutables;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.hubspot.immutables.utils.WireSafeEnum;

public class RosettaAwareWireSafeEnumDeserializer extends JsonDeserializer<WireSafeEnum<?>> implements ContextualDeserializer {
  private static final Map<Class<?>, JsonDeserializer<WireSafeEnum<?>>> DESERIALIZER_CACHE = new ConcurrentHashMap<>();

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
    return ContextualHelper.createContextual(
        ctxt::getContextualType,
        RosettaAwareWireSafeEnumDeserializer::deserializerFor,
        ctxt::reportMappingException);
  }

  @Override
  public WireSafeEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    throw ctxt.mappingException("Expected createContextual to be called");
  }

  private static <T extends Enum<T>> JsonDeserializer<?> deserializerFor(Class<?> rawType) {
    return DESERIALIZER_CACHE.computeIfAbsent(rawType, ignored -> {
      @SuppressWarnings("unchecked")
      Class<T> enumType = (Class<T>) rawType;
      return newDeserializer(enumType);
    });
  }

  private static <T extends Enum<T>> JsonDeserializer<WireSafeEnum<?>> newDeserializer(Class<T> enumType) {
    return new JsonDeserializer<WireSafeEnum<?>>() {

      @Override
      public WireSafeEnum<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
          return WireSafeEnum.of(p.getCodec().readValue(p, enumType));
        } catch (IOException e) {
          throw new IllegalStateException("Invalid value for enum type: " + enumType.getTypeName(), e);
        }
      }
    };
  }
}
