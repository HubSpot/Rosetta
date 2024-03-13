package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.Writer;

abstract class ContextualStoredAsJsonSerializer<T>
  extends NonTypedScalarSerializerBase<T> {

  private final BeanProperty property;

  ContextualStoredAsJsonSerializer(Class<T> t, BeanProperty property) {
    super(t);
    this.property = property;
  }

  protected void serializeAsBytes(
    T value,
    JsonGenerator generator,
    SerializerProvider provider,
    StdSerializer<byte[]> delegate
  ) throws IOException {
    delegate.serialize(
      serializeToBytes(value, getMapper(generator), provider),
      generator,
      provider
    );
  }

  protected void serializeAsString(
    T value,
    JsonGenerator gen,
    SerializerProvider provider
  ) throws IOException {
    String res = serializeToString(value, getMapper(gen), provider);
    if ("null".equals(res)) {
      gen.writeNull();
    } else {
      gen.writeString(res);
    }
  }

  private byte[] serializeToBytes(
    T value,
    ObjectMapper mapper,
    SerializerProvider provider
  ) throws IOException {
    try (ByteArrayBuilder array = new ByteArrayBuilder(new BufferRecycler())) {
      if (trySerialzieToArray(value, mapper, provider, array)) {
        byte[] result = array.toByteArray();
        array.release();
        return result;
      }
    }

    // fallback on old behavior
    return mapper.writeValueAsBytes(value);
  }

  private String serializeToString(
    T value,
    ObjectMapper mapper,
    SerializerProvider provider
  ) throws IOException {
    try (SegmentedStringWriter sw = new SegmentedStringWriter(new BufferRecycler())) {
      if (trySerializeToWriter(value, mapper, provider, sw)) {
        return sw.getAndClear();
      }
    }

    // fallback on old behavior
    JsonNode tree = mapper.valueToTree(value);
    if (tree.isNull()) {
      return tree.asText();
    } else {
      return mapper.writeValueAsString(tree);
    }
  }

  private boolean trySerialzieToArray(
    T value,
    ObjectMapper mapper,
    SerializerProvider provider,
    ByteArrayBuilder builder
  ) throws IOException {
    try (
      JsonGenerator gen = mapper.getFactory().createGenerator(builder, JsonEncoding.UTF8)
    ) {
      return trySerializeToGenerator(value, mapper, provider, gen);
    }
  }

  private boolean trySerializeToWriter(
    T value,
    ObjectMapper mapper,
    SerializerProvider provider,
    Writer writer
  ) throws IOException {
    try (JsonGenerator gen = mapper.getFactory().createGenerator(writer)) {
      return trySerializeToGenerator(value, mapper, provider, gen);
    }
  }

  private boolean trySerializeToGenerator(
    T value,
    ObjectMapper mapper,
    SerializerProvider provider,
    JsonGenerator gen
  ) throws IOException {
    JsonSerializer<Object> serializer = provider.findTypedValueSerializer(
      mapper
        .getTypeFactory()
        .constructSpecializedType(property.getType(), value.getClass()),
      false,
      null
    );

    if (serializer == null) {
      return false;
    }

    mapper.getSerializationConfig().initialize(gen);
    serializer.serialize(value, gen, provider);
    return true;
  }

  private ObjectMapper getMapper(JsonGenerator generator) {
    return (ObjectMapper) generator.getCodec();
  }
}
