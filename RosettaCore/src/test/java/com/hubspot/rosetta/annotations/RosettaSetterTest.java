package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.RosettaSetterBean;
import java.io.IOException;
import org.junit.Test;

public class RosettaSetterTest {

  private static final String EXPECTED_ROSETTA_JSON =
    "{\"mcCartneySongTitle\":\"Hey Jude\",\"otherValue\":\"blah\"}";
  private static final String EXPECTED_JACKSON_JSON =
    "{\"mcCartneySongTitle\":\"Hey Jude\",\"otherValue\":\"blah\"}";
  private static final String ROSETTA_JSON =
    "{\"mccartney_song_title\":\"Hey Jude\",\"jsonIgnoreRosettaUse\":\"Here\",\"other_value\":\"blah\"}";
  private static final String JACKSON_JSON =
    "{\"mcCartneySongTitle\":\"Hey Jude\",\"jsonIgnoreRosettaUse\":\"Here\",\"otherValue\":\"blah\"}";

  @Test
  public void itWritesWithThePropertyName() throws JsonProcessingException {
    RosettaSetterBean bean = new RosettaSetterBean();
    bean.setMcCartneySongTitle("Hey Jude");
    bean.setJsonIgnoreRosettaUse("Here");
    bean.setSomeOtherValue("blah");
    assertThat(Rosetta.getMapper().writeValueAsString(bean))
      .isEqualTo(EXPECTED_ROSETTA_JSON);
    assertThat(new ObjectMapper().writeValueAsString(bean))
      .isEqualTo(EXPECTED_JACKSON_JSON);
  }

  @Test
  public void itReadsThePropertyName() throws IOException {
    RosettaSetterBean rosettaRead = Rosetta
      .getMapper()
      .readValue(ROSETTA_JSON, RosettaSetterBean.class);
    assertThat(rosettaRead.getMcCartneySongTitle()).isEqualTo("Hey Jude");
    assertThat(rosettaRead.getJsonIgnoreRosettaUse()).isEqualTo("Here");
    assertThat(rosettaRead.getSomeOtherValue()).isEqualTo("blah");

    RosettaSetterBean jsonRead = new ObjectMapper()
      .readValue(JACKSON_JSON, RosettaSetterBean.class);
    assertThat(jsonRead.getJsonIgnoreRosettaUse()).isNull();
    assertThat(jsonRead.getMcCartneySongTitle()).isEqualTo("Hey Jude");
    assertThat(jsonRead.getSomeOtherValue()).isEqualTo("blah");
  }
}
