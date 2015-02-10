package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

/**
 * This wipes out any active view to make sure all fields are available for mapping/binding
 */
public final class NoViewSerializerProvider extends DefaultSerializerProvider {
  private static final long serialVersionUID = 1L;

  public NoViewSerializerProvider() {
    super();
  }

  private NoViewSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
    super(src, config, f);
  }

  @Override
  public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
    return new NoViewSerializerProvider(this, config.withView(null), jsf);
  }
}