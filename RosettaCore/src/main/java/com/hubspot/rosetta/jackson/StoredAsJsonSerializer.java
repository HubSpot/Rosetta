package com.hubspot.rosetta.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class StoredAsJsonSerializer<T> extends StdScalarSerializer<T> {

  private final String defaultValue;

  private StoredAsJsonSerializer(Class<T> klass, String defaultValue) {
    super(klass);
    this.defaultValue = defaultValue;
  }

  public static <E> StoredAsJsonSerializer<E> forType(Class<E> klass, String defaultValue) {
    return new StoredAsJsonSerializer<E>(klass, defaultValue);
  }

  @Override
  public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
      JsonGenerationException {
    if (value != null) {
      ObjectMapper mapper = (ObjectMapper) jgen.getCodec();
      jgen.writeString(mapper.writeValueAsString(value));
    } else {
      jgen.writeString(defaultValue);
    }
  }

}
