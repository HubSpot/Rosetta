package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.AnnotatedElement;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

abstract class ContextualStoredAsJsonSerializer<T>
  extends NonTypedScalarSerializerBase<T> {

  private static final ConcurrentHashMap<InclusionCacheKey, ObjectMapper> MAPPER_CACHE =
    new ConcurrentHashMap<>();

  private final BeanProperty property;
  private final JsonInclude.Include inclusion;

  ContextualStoredAsJsonSerializer(Class<T> t, BeanProperty property) {
    super(t);
    this.property = property;
    this.inclusion = findInclusion(property);
  }

  private static JsonInclude.Include findInclusion(BeanProperty property) {
    if (property == null) {
      return null;
    }
    AnnotatedMember member = property.getMember();
    if (member != null) {
      AnnotatedElement annotated = member.getAnnotated();
      if (annotated != null) {
        JsonInclude annotation = annotated.getAnnotation(JsonInclude.class);
        if (
          annotation != null && annotation.value() != JsonInclude.Include.USE_DEFAULTS
        ) {
          return annotation.value();
        }
      }
    }
    return null;
  }

  private ObjectMapper getConfiguredMapper(ObjectMapper baseMapper) {
    if (inclusion == null) {
      return baseMapper;
    }
    return MAPPER_CACHE.computeIfAbsent(
      new InclusionCacheKey(baseMapper, inclusion),
      key -> {
        ObjectMapper nestedMapper = baseMapper.copy();
        nestedMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        nestedMapper.setSerializationInclusion(key.inclusion);
        return nestedMapper;
      }
    );
  }

  private static class InclusionCacheKey {

    final int mapperIdentity;
    final JsonInclude.Include inclusion;

    InclusionCacheKey(ObjectMapper mapper, JsonInclude.Include inclusion) {
      this.mapperIdentity = System.identityHashCode(mapper);
      this.inclusion = inclusion;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof InclusionCacheKey)) {
        return false;
      }
      InclusionCacheKey that = (InclusionCacheKey) o;
      return mapperIdentity == that.mapperIdentity && inclusion == that.inclusion;
    }

    @Override
    public int hashCode() {
      return Objects.hash(mapperIdentity, inclusion);
    }
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
    if (inclusion != null) {
      return mapper.writeValueAsBytes(value);
    }

    try (ByteArrayBuilder array = new ByteArrayBuilder(new BufferRecycler())) {
      if (trySerialzieToArray(value, mapper, provider, array)) {
        byte[] result = array.toByteArray();
        array.release();
        return result;
      }
    }

    return mapper.writeValueAsBytes(value);
  }

  private String serializeToString(
    T value,
    ObjectMapper mapper,
    SerializerProvider provider
  ) throws IOException {
    if (inclusion != null) {
      return mapper.writeValueAsString(value);
    }

    try (SegmentedStringWriter sw = new SegmentedStringWriter(new BufferRecycler())) {
      if (trySerializeToWriter(value, mapper, provider, sw)) {
        return sw.getAndClear();
      }
    }

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
    ObjectMapper baseMapper = (ObjectMapper) generator.getCodec();
    return getConfiguredMapper(baseMapper);
  }
}
