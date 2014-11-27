package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class StoredAsJsonDeserializer<T> extends StdScalarDeserializer<T> {

  private static final long serialVersionUID = 1L;
  private final Type type;
  private final String defaultValue;

  public StoredAsJsonDeserializer(Class<T> vc, Type type, String defaultValue) {
    super(vc);
    this.type = type;
    this.defaultValue = defaultValue;
  }

  @Override
  public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    JavaType javaType = ctxt.getTypeFactory().constructType(type);
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();

    if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
      return deserialize(mapper, null, javaType);
    } else if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
      return deserialize(mapper, jp.getText(), javaType);
    } else if(jp.getCurrentToken() == JsonToken.START_OBJECT || jp.getCurrentToken() == JsonToken.START_ARRAY) {
      return mapper.readValue(jp, javaType);
    } else {
      throw ctxt.mappingException("Expected JSON String");
    }
  }

  private T deserialize(ObjectMapper mapper, String json, JavaType javaType) throws IOException {
    if (json == null || json.isEmpty()) {
      json = defaultValue;
    }

    if (json == null) {
      return null;
    } else {
      return mapper.readValue(json, javaType);
    }
  }
}
