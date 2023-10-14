package com.hubspot.rosetta.internal;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StoredAsJsonDeserializer<T> extends StdScalarDeserializer<T> {
  private static final long serialVersionUID = 1L;

  private final Type type;
  private final String defaultValue;
  private final ObjectMapper objectMapper;

  private final boolean storedAsBinary;

  public StoredAsJsonDeserializer(Class<T> vc, Type type, String defaultValue, ObjectMapper objectMapper, boolean storedAsBinary) {
    super(vc);
    this.type = type;
    this.defaultValue = defaultValue;
    this.objectMapper = objectMapper;
    this.storedAsBinary = storedAsBinary;
  }

  @Override
  public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    JavaType javaType = ctxt.getTypeFactory().constructType(type);
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();

    if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
      if (storedAsBinary) {
        return deserializeAsBinary(jp, javaType, mapper);
      } else {
        return deserialize(mapper, jp.getText(), javaType);
      }
    } else if (jp.getCurrentToken() == JsonToken.VALUE_EMBEDDED_OBJECT) {
      return deserializeAsBinary(jp, javaType, mapper);
    } else if (jp.getCurrentToken() == JsonToken.START_OBJECT || jp.getCurrentToken() == JsonToken.START_ARRAY) {
      return mapper.readValue(jp, javaType);
    } else {
      throw ctxt.mappingException("Expected JSON String");
    }
  }

  private T deserializeAsBinary(JsonParser jp, JavaType javaType, ObjectMapper mapper) throws IOException {
    String json = new String(jp.getBinaryValue(Base64Variants.getDefaultVariant()), StandardCharsets.UTF_8);
    return deserialize(mapper, json, javaType);
  }

  @Override
  public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
    // we're delegating to the our parent object mapper, so the TypeDeserializer doesn't matter
    return deserialize(jp, ctxt);
  }

  @Override
  public T getNullValue() {
    try {
      return deserialize(objectMapper, null, objectMapper.constructType(type));
    } catch (IOException e) {
      throw new RuntimeException(e);
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
