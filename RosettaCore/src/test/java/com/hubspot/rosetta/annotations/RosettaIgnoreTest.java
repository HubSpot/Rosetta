package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.hubspot.rosetta.Rosetta;

public class RosettaIgnoreTest {
  private static final String JSON = JsonNodeFactory.instance
      .objectNode()
      .put("stringProperty", "value")
      .toString();
  private static final String JSON_WITH_IGNORED = JsonNodeFactory.instance
      .objectNode()
      .put("stringProperty", "value")
      .put("ignoredProperty", "ignored")
      .toString();

  @Test
  public void itIgnoresMethodsWhenSerializing() throws JsonProcessingException {
    IgnoreMethodBean bean = new IgnoreMethodBean();
    assertThat(Rosetta.getMapper().writeValueAsString(bean))
        .isEqualTo(JSON);
  }

  @Test
  public void itIgnoresFieldsWhenSerializing() throws JsonProcessingException {
    IgnoreFieldBean bean = new IgnoreFieldBean();
    assertThat(Rosetta.getMapper().writeValueAsString(bean))
        .isEqualTo(JSON);
  }

  @Test
  public void itIgnoresIdenticallyNamedProperties() throws JsonProcessingException {
    DuplicatePropertiesBean bean = new DuplicatePropertiesBean();
    assertThat(Rosetta.getMapper().writeValueAsString(bean))
        .isEqualTo(JSON);
  }

  @Test
  public void itIgnoresMethodsWhenDeserializing() throws IOException {
    IgnoreFieldMethodSetterBean bean = Rosetta.getMapper()
        .readValue(JSON_WITH_IGNORED, IgnoreFieldMethodSetterBean.class);
    assertThat(bean).isNotNull();
    assertThat(bean.stringProperty).isEqualTo("value");
    assertThat(bean.ignoredProperty).isNull();
  }

  @Test
  public void itIgnoresFieldsWhenDeserializing() throws IOException {
    IgnoreFieldSetterBean bean = Rosetta.getMapper()
        .readValue(JSON_WITH_IGNORED, IgnoreFieldSetterBean.class);
    assertThat(bean).isNotNull();
    assertThat(bean.stringProperty).isEqualTo("value");
    assertThat(bean.ignoredProperty).isNull();
  }

  @Test
  public void itIgnoresConstructorsWhenDeserializing() throws IOException {
    IgnoreConstructorBean bean = Rosetta.getMapper()
        .readValue(JSON_WITH_IGNORED, IgnoreConstructorBean.class);
    assertThat(bean).isNotNull();
    assertThat(bean.stringProperty).isEqualTo("value");
    assertThat(bean.ignoredProperty).isNull();
  }

  @Test
  public void itIgnoresJsonValueWhenSerializing() throws JsonProcessingException {
    IgnoreJsonValueBean bean = new IgnoreJsonValueBean();
    assertThat(Rosetta.getMapper().writeValueAsString(bean))
        .isEqualTo(Integer.toString(bean.getId()));
  }

  private static class IgnoreMethodBean {

    public String getStringProperty() {
      return "value";
    }

    @RosettaIgnore
    public String getIgnoredProperty() {
      return "ignored";
    }
  }

  private static class IgnoreFieldBean {
    @JsonProperty
    private final String stringProperty = "value";

    @RosettaIgnore
    @JsonProperty
    private final String ignoredProperty = "ignored";
  }

  private static class DuplicatePropertiesBean {
    @RosettaProperty("stringProperty")
    private final String includedProperty = "value";

    @RosettaIgnore
    @JsonProperty("stringProperty")
    private final String ignoredProperty = "value2";
  }

  private static class IgnoreFieldMethodSetterBean {
    private String stringProperty;
    private String ignoredProperty;

    public void setStringProperty(String stringProperty) {
      this.stringProperty = stringProperty;
    }

    @RosettaIgnore
    public void setIgnoredProperty(String ignoredProperty) {
      this.ignoredProperty = ignoredProperty;
    }
  }

  private static class IgnoreFieldSetterBean {
    @JsonProperty
    private String stringProperty;

    @JsonProperty
    @RosettaIgnore
    private String ignoredProperty;
  }

  private static class IgnoreConstructorBean {
    @JsonProperty
    private String stringProperty;

    @JsonProperty
    @RosettaIgnore
    private String ignoredProperty;

    IgnoreConstructorBean() {}

    @JsonCreator
    @RosettaIgnore
    IgnoreConstructorBean(
        @JsonProperty("stringProperty") String stringProperty,
        @JsonProperty("ignoredProperty") String ignoredProperty
    ) {
      this.stringProperty = stringProperty;
      this.ignoredProperty = ignoredProperty;
    }
  }

  private class IgnoreJsonValueBean {

    @RosettaValue
    public int getId() {
      return 1;
    }

    @JsonValue
    @RosettaIgnore
    public String getCode() {
      return "a";
    }
  }
}
