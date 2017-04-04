package com.hubspot.rosetta.internal;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

public class ConstantSerializer extends NonTypedScalarSerializerBase<Object> {
  private static final StringSerializer DELEGATE = new StringSerializer();

  private final String value;

  public ConstantSerializer(String value) {
    super(Object.class);
    this.value = value;
  }

  @Override
  public void serialize(Object ignored, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    jgen.writeString(value);
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
