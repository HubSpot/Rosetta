package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.RosettaCreatorConstructorBean;
import com.hubspot.rosetta.beans.RosettaCreatorMethodBean;
import java.io.IOException;
import org.junit.Test;

public class RosettaCreatorTest {

  private static final String JSON = "{\"stringProperty\":\"value\"}";

  @Test
  public void itWorksOnConstructors() throws IOException {
    RosettaCreatorConstructorBean bean = Rosetta
      .getMapper()
      .readValue(JSON, RosettaCreatorConstructorBean.class);
    assertThat(bean.getStringProperty()).isEqualTo("value");
  }

  @Test
  public void itWorksOnMethods() throws IOException {
    RosettaCreatorMethodBean bean = Rosetta
      .getMapper()
      .readValue(JSON, RosettaCreatorMethodBean.class);
    assertThat(bean.getStringProperty()).isEqualTo("value");
  }
}
