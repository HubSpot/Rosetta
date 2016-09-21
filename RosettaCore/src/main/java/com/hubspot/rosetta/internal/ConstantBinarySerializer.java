package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class ConstantBinarySerializer extends StdSerializer<Object> {
  private static final StdSerializer<byte[]> DELEGATE = findDelegate();

  private final byte[] value;

  public ConstantBinarySerializer(String value) {
    super(Object.class);
    this.value = value.getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public void serialize(Object ignored, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    DELEGATE.serialize(value, jgen, provider);
  }

  @Override
  public void serializeWithType(Object ignored, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    DELEGATE.serializeWithType(value, gen, serializers, typeSer);
  }

  @Override
  public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
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

  private static StdSerializer<byte[]> findDelegate() {
    try {
      // Jackson 2.6+
      return newInstance("com.fasterxml.jackson.databind.ser.std.ByteArraySerializer");
    } catch (Throwable t) {
      try {
        return newInstance("com.fasterxml.jackson.databind.ser.std.StdArraySerializers$ByteArraySerializer");
      } catch (Throwable t2) {
        throw new RuntimeException("Unable to find ByteArraySerializer to delegate to", t2);
      }
    }
  }

  private static StdSerializer<byte[]> newInstance(String className) throws Exception {
    return (StdSerializer<byte[]>) Class.forName(className).newInstance();
  }
}
