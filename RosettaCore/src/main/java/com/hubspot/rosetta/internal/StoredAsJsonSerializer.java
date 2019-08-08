package com.hubspot.rosetta.internal;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

public class StoredAsJsonSerializer<T> extends NonTypedScalarSerializerBase<T> implements ContextualSerializer {
  private static final StringSerializer DELEGATE = new StringSerializer();

  public StoredAsJsonSerializer(Class<T> t) {
    super(t);
  }

  @Override
  public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    ObjectMapper mapper = (ObjectMapper) jgen.getCodec();
    JsonNode tree = mapper.valueToTree(value);
    if (tree.isNull()) {
      provider.defaultSerializeNull(jgen);
    } else {
      jgen.writeString(mapper.writeValueAsString(tree));
    }
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

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
    if (property == null) {
      return this;
    } else {
      return new ContextualStoredAsJsonSerializer<>(_handledType, property);
    }
  }

  private static class ContextualStoredAsJsonSerializer<T> extends NonTypedScalarSerializerBase<T> {
    private final BeanProperty property;

    ContextualStoredAsJsonSerializer(Class<T> t, BeanProperty property) {
      super(t);
      this.property = property;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      ObjectMapper mapper = (ObjectMapper) gen.getCodec();
      if (value == null) {
        provider.defaultSerializeNull(gen);
      }

      JsonSerializer<Object> serializer = provider.findTypedValueSerializer(resolveRealType(value, mapper), false, null);
      if (serializer != null) {
        SegmentedStringWriter sw = new SegmentedStringWriter(new BufferRecycler());
        JsonGenerator subGen = mapper.getFactory().createGenerator(sw);
        try {
          mapper.getSerializationConfig().initialize(subGen);
          serializer.serialize(value, subGen, provider);
        } finally {
          subGen.close();
        }
        gen.writeString(sw.getAndClear());
      } else {

      }
    }

    private JavaType resolveRealType(T value, ObjectMapper mapper) {
      if (property.getType().hasGenericTypes()) {
        return property.getType();
      } else {
        return mapper.getTypeFactory().constructSpecializedType(property.getType(), value.getClass());
      }
    }
  }
}
