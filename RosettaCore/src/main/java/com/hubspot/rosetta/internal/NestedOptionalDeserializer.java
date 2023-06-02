package com.hubspot.rosetta.internal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class NestedOptionalDeserializer<T> extends StdScalarDeserializer<Optional<T>> {

  private final Class<T> referencedClazz;

  public NestedOptionalDeserializer(Class<Optional<T>> clazz, Class<T> referencedClazz) {
    super(clazz);
    this.referencedClazz = referencedClazz;
  }

  @Override
  public Optional<T> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();

    JsonNode root = mapper.readValue(jp, JsonNode.class);

    return convert(root, mapper);
  }

  private Optional<T> convert(JsonNode root, ObjectMapper mapper) throws IOException {
    if (root.isNull()) {
      return Optional.empty();
    }

    Iterator<Entry<String, JsonNode>> it = root.fields();

    if (!it.hasNext()) {
      throw new IllegalArgumentException("The provided object has no fields: " + root);
    }

    while (it.hasNext()) {
      Entry<String, JsonNode> entry = it.next();

      if (!entry.getValue().isNull()) {
        return Optional.of(
            mapper.treeToValue(root, referencedClazz)
        );
      }
    }

    return Optional.empty();
  }

}
