package com.hubspot.rosetta;

import org.junit.Test;

import com.hubspot.rosetta.beans.Person;
import com.hubspot.rosetta.tablets.BeanTabletFactory;

import static org.fest.assertions.api.Assertions.assertThat;

public class BeanTabletFactoryTestCase {

  private static final TabletFactory factory = new BeanTabletFactory();

  @Test
  public void acceptedTypes() {
    assertThat(factory.accepts(Integer.class)).isFalse();
    assertThat(factory.accepts(Person.class)).isTrue();
  }

}
