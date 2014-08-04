package com.hubspot.rosetta.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.google.common.base.Strings;

public class StoredAsJsonDeserializer<T> extends StdScalarDeserializer<T> {

  private static final long serialVersionUID = 1L;
  private final Class<T> klass;
  private final String defaultValue;

  private StoredAsJsonDeserializer(Class<T> klass, String defaultValue) {
    super(klass);
    this.klass = klass;
    this.defaultValue = defaultValue;
  }

  @Override
  public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();
    if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
      String valueAsString = jp.getValueAsString();
      if (Strings.isNullOrEmpty(valueAsString)) {
        valueAsString = !Strings.isNullOrEmpty(defaultValue) ? defaultValue : "{}";
      }
      return mapper.readValue(valueAsString, klass);
    }
    else if(jp.getCurrentToken() == JsonToken.START_OBJECT || jp.getCurrentToken() == JsonToken.START_ARRAY) {
      return mapper.readValue(jp, klass);
    }
    throw ctxt.mappingException("Expected JSON String");
  }

  public static <E> StoredAsJsonDeserializer<E> forType(Class<E> klass, String defaultValue) {
    return new StoredAsJsonDeserializer<E>(klass, defaultValue);
  }

}
