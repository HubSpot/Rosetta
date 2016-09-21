package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class StoredAsJsonBinarySerializer<T> extends StdSerializer<T> {
  private static final StdSerializer<byte[]> DELEGATE = findDelegate();

  public StoredAsJsonBinarySerializer(Class<T> t) {
    super(t);
  }

  @Override
  public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    ObjectMapper mapper = (ObjectMapper) jgen.getCodec();
    DELEGATE.serialize(mapper.writeValueAsBytes(value), jgen, provider);
  }

  @Override
  public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
    return DELEGATE.getSchema(provider, typeHint);
  }

  @Override
  public void serializeWithType(T value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    ObjectMapper mapper = (ObjectMapper) gen.getCodec();
    DELEGATE.serializeWithType(mapper.writeValueAsBytes(value), gen, serializers, typeSer);
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
        throw new RuntimeException("Unable to find ByteArraySerializer to delegate to", f);
      }
    }
  }

  private static StdSerializer<byte[]> newInstance(String className) throws Exception {
    return (StdSerializer<byte[]>) Class.forName(className).newInstance();
  }
}
