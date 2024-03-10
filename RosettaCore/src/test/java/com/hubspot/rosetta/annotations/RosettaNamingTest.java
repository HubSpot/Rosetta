package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.RosettaNamingBean;
import java.io.IOException;
import org.junit.Test;

public class RosettaNamingTest {

  private static final String JSON = "{\"string_property\":\"value\"}";

  @Test
  public void itWritesUnderscoreJson() throws JsonProcessingException {
    RosettaNamingBean bean = new RosettaNamingBean();
    bean.setStringProperty("value");

    assertThat(Rosetta.getMapper().writeValueAsString(bean)).isEqualTo(JSON);
  }

  @Test
  public void itReadsUnderscoreJson() throws IOException {
    assertThat(
      Rosetta.getMapper().readValue(JSON, RosettaNamingBean.class).getStringProperty()
    )
      .isEqualTo("value");
  }
}
