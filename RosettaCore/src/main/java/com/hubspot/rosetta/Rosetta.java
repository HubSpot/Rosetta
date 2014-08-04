package com.hubspot.rosetta;

import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Lists;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import com.hubspot.rosetta.jackson.RosettaAnnotationIntrospector;
import com.hubspot.rosetta.tablets.BeanTabletFactory;

/**
 * Static public APIs to get/set some Rosetta globals.
 */
public final class Rosetta {
  private Rosetta() {}

  private static final List<TabletFactory> tabletFactories = Lists.<TabletFactory>newArrayList(new BeanTabletFactory());

  private static final ObjectMapper ourMapper = new ObjectMapper()
      .setAnnotationIntrospector(new AnnotationIntrospectorPair(new RosettaAnnotationIntrospector(), new JacksonAnnotationIntrospector()))
      .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new GuavaModule())
      .registerModule(new JodaModule())
      .registerModule(new ProtobufModule());

  /**
   * Register a new {@link TabletFactory}.
   */
  public static void registerTablet(TabletFactory tabletFactory) {
    tabletFactories.add(0, tabletFactory);
  }

  /**
   * Get the table for a given type, or null.
   */
  public static Tablet tabletForType(Class<?> klass) {
    Tablet theTablet = null;

    for (final TabletFactory t : tabletFactories) {
      if (t.accepts(klass)) {
        theTablet = t.getInstance(klass);
        theTablet.setMapper(getMapper());
        break;
      }
    }
    return theTablet;
  }

  /**
   * If you really just want an {@link ObjectMapper}, use this. Hint: you probably don't.
   */
  public static ObjectMapper getMapper() {
    return ourMapper;
  }
}
