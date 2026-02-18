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
  private final ObjectMapper mapper;
  private final ObjectMapper baseMapper;
  private volatile ObjectMapper cachedOuterMapper;
  private volatile ObjectMapper cachedSafeMapper;

  ContextualStoredAsJsonSerializer(
    Class<T> t,
    BeanProperty property,
    ObjectMapper mapper
  ) {
    this(t, property, mapper, null);
  }

  ContextualStoredAsJsonSerializer(
    Class<T> t,
    BeanProperty property,
    ObjectMapper mapper,
    ObjectMapper baseMapper
  ) {
    super(t);
    this.property = property;
    this.mapper = mapper;
    this.baseMapper = baseMapper;
  }

  private ObjectMapper resolveMapper(JsonGenerator gen) {
    if (mapper == null) {
      return (ObjectMapper) gen.getCodec();
    }
    if (baseMapper == null) {
      return mapper;
    }
    ObjectMapper outerMapper = (ObjectMapper) gen.getCodec();
    if (outerMapper == baseMapper) {
      return mapper;
    }
    ObjectMapper cached = cachedSafeMapper;
    if (cached != null && cachedOuterMapper == outerMapper) {
      return cached;
    }
    ObjectMapper safe = RosettaAnnotationIntrospector.createSafeMapper(outerMapper);
    cachedOuterMapper = outerMapper;
    cachedSafeMapper = safe;
    return safe;
  }

  protected void serializeAsBytes(
    T value,
    JsonGenerator generator,
    SerializerProvider provider,
    StdSerializer<byte[]> delegate
  ) throws IOException {
    ObjectMapper effectiveMapper = resolveMapper(generator);
    delegate.serialize(serializeToBytes(value, effectiveMapper), generator, provider);
  }

  protected void serializeAsString(
    T value,
    JsonGenerator gen,
    SerializerProvider provider
  ) throws IOException {
    ObjectMapper effectiveMapper = resolveMapper(gen);
    String res = serializeToString(value, effectiveMapper);
    if ("null".equals(res)) {
      gen.writeNull();
    } else {
      gen.writeString(res);
    }
  }

  private byte[] serializeToBytes(T value, ObjectMapper effectiveMapper)
    throws IOException {
    try (ByteArrayBuilder array = new ByteArrayBuilder(new BufferRecycler())) {
      if (trySerialzieToArray(value, array, effectiveMapper)) {
        byte[] result = array.toByteArray();
        array.release();
        return result;
      }
    }

    // fallback on old behavior
    return effectiveMapper.writeValueAsBytes(value);
  }

  private String serializeToString(T value, ObjectMapper effectiveMapper)
    throws IOException {
    try (SegmentedStringWriter sw = new SegmentedStringWriter(new BufferRecycler())) {
      if (trySerializeToWriter(value, sw, effectiveMapper)) {
        return sw.getAndClear();
      }
    }

    // fallback on old behavior
    JsonNode tree = effectiveMapper.valueToTree(value);
    if (tree.isNull()) {
      return tree.asText();
    } else {
      return effectiveMapper.writeValueAsString(tree);
    }
  }

  private boolean trySerialzieToArray(
    T value,
    ByteArrayBuilder builder,
    ObjectMapper effectiveMapper
  ) throws IOException {
    try (
      JsonGenerator gen = effectiveMapper
        .getFactory()
        .createGenerator(builder, JsonEncoding.UTF8)
    ) {
      return trySerializeToGenerator(value, gen, effectiveMapper);
    }
  }

  private boolean trySerializeToWriter(
    T value,
    Writer writer,
    ObjectMapper effectiveMapper
  ) throws IOException {
    try (JsonGenerator gen = effectiveMapper.getFactory().createGenerator(writer)) {
      return trySerializeToGenerator(value, gen, effectiveMapper);
    }
  }

  private boolean trySerializeToGenerator(
    T value,
    JsonGenerator gen,
    ObjectMapper effectiveMapper
  ) throws IOException {
    SerializerProvider provider = effectiveMapper.getSerializerProviderInstance();

    JsonSerializer<Object> serializer = provider.findTypedValueSerializer(
      effectiveMapper
        .getTypeFactory()
        .constructSpecializedType(property.getType(), value.getClass()),
      false,
      null
    );

    if (serializer == null) {
      return false;
    }

    effectiveMapper.getSerializationConfig().initialize(gen);
    serializer.serialize(value, gen, provider);
    return true;
  }
}
