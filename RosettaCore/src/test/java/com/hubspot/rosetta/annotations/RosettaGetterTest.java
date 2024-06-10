package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.RosettaGetterBean;
import java.io.IOException;
import org.junit.Test;

public class RosettaGetterTest {

  private static final String EXPECTED_ROSETTA_JSON =
    "{\"jsonIgnoreRosettaUse\":\"Here\",\"mccartney_song_title\":\"Hey Jude\",\"other_value\":\"blah\"}";
  private static final String ROSETTA_JSON =
    "{\"jsonIgnoreRosettaUse\":\"Here\",\"mcCartneySongTitle\":\"Hey Jude\",\"otherValue\":\"blah\"}";
  private static final String JACKSON_JSON =
    "{\"mcCartneySongTitle\":\"Hey Jude\",\"otherValue\":\"blah\"}";

  @Test
  public void itWritesWithTheGetterName() throws JsonProcessingException {
    RosettaGetterBean bean = new RosettaGetterBean();
    bean.setMcCartneySongTitle("Hey Jude");
    bean.setJsonIgnoreRosettaUse("Here");
    bean.setSomeOtherValue("blah");
    assertThat(Rosetta.getMapper().writeValueAsString(bean))
      .isEqualTo(EXPECTED_ROSETTA_JSON);
    assertThat(new ObjectMapper().writeValueAsString(bean)).isEqualTo(JACKSON_JSON);
  }

  @Test
  public void itReadsThePropertyName() throws IOException {
    RosettaGetterBean rosettaRead = Rosetta
      .getMapper()
      .readValue(ROSETTA_JSON, RosettaGetterBean.class);
    assertThat(rosettaRead.getMcCartneySongTitle()).isEqualTo("Hey Jude");
    assertThat(rosettaRead.getJsonIgnoreRosettaUse()).isNull();
    assertThat(rosettaRead.getSomeOtherValue()).isEqualTo("blah");

    RosettaGetterBean jsonRead = new ObjectMapper()
      .readValue(JACKSON_JSON, RosettaGetterBean.class);
    assertThat(jsonRead.getJsonIgnoreRosettaUse()).isNull();
    assertThat(jsonRead.getMcCartneySongTitle()).isEqualTo("Hey Jude");
    assertThat(jsonRead.getSomeOtherValue()).isEqualTo("blah");
  }
}
