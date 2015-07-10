package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.ByteArraySerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class ConstantBinarySerializer extends StdSerializer<Object> {
  private static final ByteArraySerializer DELEGATE = new ByteArraySerializer();

  private final byte[] value;

  public ConstantBinarySerializer(byte[] value) {
    super(Object.class);
    this.value = value;
  }

  @Override
  public void serialize(Object ignored, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    DELEGATE.serialize(value, jgen, provider);
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
