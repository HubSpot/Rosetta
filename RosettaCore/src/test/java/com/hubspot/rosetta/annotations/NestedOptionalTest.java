package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.InnerBean;
import com.hubspot.rosetta.beans.NestedOptionalBean;

public class NestedOptionalTest {

  @Test
  public void testAnnotatedFieldDeserialization() throws IOException {
    String nestedOptionalBeanJson = "" +
        "{\"firstNestedOptional\":{\"stringProperty\":\"value-1\"}, " +
        "\"secondNestedOptional\":{\"firstStringProperty\": null, \"secondStringProperty\": null}}";

    NestedOptionalBean bean = Rosetta.getMapper().readValue(nestedOptionalBeanJson, NestedOptionalBean.class);

    assertThat(bean.getFirstNestedOptional().map(InnerBean::getStringProperty)).contains("value-1");
    assertThat(bean.getSecondNestedOptional()).isEmpty();
  }

}
