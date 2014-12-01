package com.hubspot.rosetta;

import com.fasterxml.jackson.databind.JsonNode;
import com.hubspot.rosetta.RosettaBinder.Callback;
import com.hubspot.rosetta.beans.InnerBean;
import com.hubspot.rosetta.beans.NestedBean;
import com.hubspot.rosetta.beans.RosettaCreatorConstructorBean;
import com.hubspot.rosetta.beans.RosettaCreatorMethodBean;
import com.hubspot.rosetta.beans.RosettaNamingBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class RosettaBinderTest {

  @Test
  public void itBindsRosettaCreatorConstructorBeanCorrectly() {
    RosettaCreatorConstructorBean bean = new RosettaCreatorConstructorBean("value");

    assertThat(bind(bean)).isEqualTo(map("stringProperty", "value"));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map("prefix.stringProperty", "value"));
  }

  @Test
  public void itBindsRosettaCreatorMethodBeanCorrectly() {
    RosettaCreatorMethodBean bean = RosettaCreatorMethodBean.fromString("value");

    assertThat(bind(bean)).isEqualTo(map("stringProperty", "value"));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map("prefix.stringProperty", "value"));
  }

  @Test
  public void itBindsRosettaNamingBeanCorrectly() {
    RosettaNamingBean bean = new RosettaNamingBean();
    bean.setStringProperty("value");

    assertThat(bind(bean)).isEqualTo(map("string_property", "value"));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map("prefix.string_property", "value"));
  }

  @Test
  public void itBindsStoredAsJsonBeanCorrectly() {
    InnerBean inner = new InnerBean();
    inner.setStringProperty("value");

    StoredAsJsonBean bean = new StoredAsJsonBean();
    bean.setAnnotatedField(inner);
    bean.setAnnotatedGetter(inner);
    bean.setAnnotatedSetter(inner);
    bean.setAnnotatedFieldWithDefault(inner);
    bean.setAnnotatedGetterWithDefault(inner);
    bean.setAnnotatedSetterWithDefault(inner);

    String json = "{\"stringProperty\":\"value\"}";

    assertThat(bind(bean)).isEqualTo(map(
            "annotatedField", json,
            "annotatedGetter", json,
            "annotatedSetter", json,
            "annotatedFieldWithDefault", json,
            "annotatedGetterWithDefault", json,
            "annotatedSetterWithDefault", json
    ));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map(
            "prefix.annotatedField", json,
            "prefix.annotatedGetter", json,
            "prefix.annotatedSetter", json,
            "prefix.annotatedFieldWithDefault", json,
            "prefix.annotatedGetterWithDefault", json,
            "prefix.annotatedSetterWithDefault", json
    ));
  }

  @Test
  public void itBindsNullStoredAsJsonBeanCorrectly() {
    StoredAsJsonBean bean = new StoredAsJsonBean();

    String json = "{\"stringProperty\":\"value\"}";

    assertThat(bind(bean)).isEqualTo(map(
            "annotatedField", null,
            "annotatedGetter", null,
            "annotatedSetter", null,
            "annotatedFieldWithDefault", json,
            "annotatedGetterWithDefault", json,
            "annotatedSetterWithDefault", json
    ));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map(
            "prefix.annotatedField", null,
            "prefix.annotatedGetter", null,
            "prefix.annotatedSetter", null,
            "prefix.annotatedFieldWithDefault", json,
            "prefix.annotatedGetterWithDefault", json,
            "prefix.annotatedSetterWithDefault", json
    ));
  }

  @Test
  public void itBindsNestedBeanCorrectly() {
    InnerBean inner = new InnerBean();
    inner.setStringProperty("value");

    NestedBean bean = new NestedBean();
    bean.setInner(inner);

    assertThat(bind(bean)).isEqualTo(map("inner.stringProperty", "value"));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map("prefix.inner.stringProperty", "value"));
  }

  private static Map<String, Object> map(String... strings) {
    Map<String, Object> map = new HashMap<String, Object>();
    for (int i = 0; i < strings.length; i += 2) {
      map.put(strings[i], strings[i + 1]);
    }

    return map;
  }

  private Map<String, Object> bind(Object o) {
    return bindWithPrefix("", o);
  }

  private Map<String, Object> bindWithPrefix(String prefix, Object o) {
    JsonNode node = Rosetta.getMapper().valueToTree(o);
    MockCallback callback = new MockCallback();
    RosettaBinder.INSTANCE.bind(prefix, node, callback);
    return callback.getBindings();
  }

  private static class MockCallback implements Callback {
    private final Map<String, Object> bindings = new HashMap<String, Object>();

    @Override
    public void bind(String key, Object value) {
      bindings.put(key, value);
    }

    public Map<String, Object> getBindings() {
      return bindings;
    }
  }
}
