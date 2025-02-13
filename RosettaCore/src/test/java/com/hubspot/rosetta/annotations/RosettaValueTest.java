package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.HasSingleValueWrapperPropBean;
import com.hubspot.rosetta.beans.RosettaValueBean;
import com.hubspot.rosetta.beans.SingleValueWrapperBean;
import org.junit.Test;

public class RosettaValueTest {

  @Test
  public void itCallsRosettaValueMethod() throws JsonProcessingException {
    RosettaValueBean bean = new RosettaValueBean();
    bean.setStringProperty("value");

    assertThat(Rosetta.getMapper().writeValueAsString(bean)).isEqualTo("\"value\"");
  }

  @Test
  public void itCallsJacksonValueMethod() throws JsonProcessingException {
    HasSingleValueWrapperPropBean bean = new HasSingleValueWrapperPropBean();
    bean.setSingleValueWrapper(SingleValueWrapperBean.of("foo"));

    assertThat(Rosetta.getMapper().writeValueAsString(bean))
      .isEqualTo("{\"singleValueWrapper\":\"foo\"}");
  }
}
