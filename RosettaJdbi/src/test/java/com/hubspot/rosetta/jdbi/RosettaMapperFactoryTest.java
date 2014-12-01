package com.hubspot.rosetta.jdbi;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.api.Assertions.assertThat;

public class RosettaMapperFactoryTest {

  private final RosettaMapperFactory mapperFactory = new RosettaMapperFactory();

  @Test
  public void itRejectsInteger() {
    assertThat(mapperFactory.accepts(Integer.class, null)).isFalse();
  }

  @Test
  public void itRejectsInt() {
    assertThat(mapperFactory.accepts(Integer.TYPE, null)).isFalse();
  }

  @Test
  public void itRejectsAtomicInteger() {
    assertThat(mapperFactory.accepts(AtomicInteger.class, null)).isFalse();
  }

}
