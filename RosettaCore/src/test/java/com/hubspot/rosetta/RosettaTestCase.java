package com.hubspot.rosetta;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RosettaTestCase {

  @Test
  public void tabletForTypeCanReturnNull() {
    assertThat(Rosetta.tabletForType(JsonIgnore.class)).isNull();
  }
}
