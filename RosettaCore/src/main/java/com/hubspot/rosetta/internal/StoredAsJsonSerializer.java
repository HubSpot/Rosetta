package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class StoredAsJsonSerializer<T> extends NonTypedScalarSerializerBase<T> {
  private static final StringSerializer DELEGATE = new StringSerializer();

  public StoredAsJsonSerializer(Class<T> t) {
    super(t);
  }

  @Override
  public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    ObjectMapper mapper = (ObjectMapper) jgen.getCodec();
    DELEGATE.serialize(mapper.writeValueAsString(value), jgen, provider);
  }

  @Override
  public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
    return DELEGATE.getSchema(provider, typeHint);
  }

  @Override
  public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) {
    // try/catch is for 2.1.x compatibility
    try {
      DELEGATE.acceptJsonFormatVisitor(visitor, typeHint);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
