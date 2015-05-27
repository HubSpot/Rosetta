package com.hubspot.rosetta.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.RosettaValueBean;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RosettaValueTest {

  @Test
  public void itCallsRosettaValueMethod() throws JsonProcessingException {
    RosettaValueBean bean = new RosettaValueBean();
    bean.setStringProperty("value");

    assertThat(Rosetta.getMapper().writeValueAsString(bean)).isEqualTo("\"value\"");
  }
}
