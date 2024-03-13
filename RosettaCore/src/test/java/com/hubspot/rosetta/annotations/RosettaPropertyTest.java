package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.RosettaPropertyBean;
import java.io.IOException;
import org.junit.Test;

public class RosettaPropertyTest {

  private static final String JSON =
    "{\"jsonIgnoreRosettaUse\":\"Here\",\"mccartney_song_title\":\"Hey Jude\"}";
  private static final String JACKSON_JSON = "{\"mcCartneySongTitle\":\"Hey Jude\"}";

  @Test
  public void itWritesWithThePropertyName() throws JsonProcessingException {
    RosettaPropertyBean bean = new RosettaPropertyBean();
    bean.setMcCartneySongTitle("Hey Jude");
    bean.setJsonIgnoreRosettaUse("Here");
    assertThat(Rosetta.getMapper().writeValueAsString(bean)).isEqualTo(JSON);
    assertThat(new ObjectMapper().writeValueAsString(bean)).isEqualTo(JACKSON_JSON);
  }

  @Test
  public void itReadsThePropertyName() throws IOException {
    RosettaPropertyBean rosettaRead = Rosetta
      .getMapper()
      .readValue(JSON, RosettaPropertyBean.class);
    assertThat(rosettaRead.getMcCartneySongTitle()).isEqualTo("Hey Jude");
    assertThat(rosettaRead.getJsonIgnoreRosettaUse()).isEqualTo("Here");
    assertThat(
      new ObjectMapper()
        .readValue(JACKSON_JSON, RosettaPropertyBean.class)
        .getMcCartneySongTitle()
    )
      .isEqualTo("Hey Jude");
  }
}
