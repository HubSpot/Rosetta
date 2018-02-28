package com.hubspot.rosetta;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Optional;
import com.hubspot.rosetta.RosettaBinder.Callback;
import com.hubspot.rosetta.beans.InnerBean;
import com.hubspot.rosetta.beans.ListBean;
import com.hubspot.rosetta.beans.NestedBean;
import com.hubspot.rosetta.beans.RosettaCreatorConstructorBean;
import com.hubspot.rosetta.beans.RosettaCreatorMethodBean;
import com.hubspot.rosetta.beans.RosettaNamingBean;
import com.hubspot.rosetta.beans.ServiceLoaderBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import com.hubspot.rosetta.beans.StoredAsJsonTypeInfoBean;
import com.hubspot.rosetta.beans.StoredAsJsonTypeInfoBean.ConcreteStoredAsJsonTypeInfo;

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

    StoredAsJsonTypeInfoBean typeInfoBean = new ConcreteStoredAsJsonTypeInfo();

    JsonNode innerJsonNode = Rosetta.getMapper().createObjectNode().set("stringProperty", TextNode.valueOf("value"));

    StoredAsJsonBean bean = new StoredAsJsonBean();
    bean.setAnnotatedField(inner);
    bean.setAnnotatedGetter(inner);
    bean.setAnnotatedSetter(inner);
    bean.setAnnotatedFieldWithDefault(inner);
    bean.setAnnotatedGetterWithDefault(inner);
    bean.setAnnotatedSetterWithDefault(inner);
    bean.setOptionalField(Optional.of(inner));
    bean.setOptionalGetter(Optional.of(inner));
    bean.setOptionalSetter(Optional.of(inner));
    bean.setBinaryField(inner);
    bean.setBinaryFieldWithDefault(inner);
    bean.setJsonNodeField(innerJsonNode);
    bean.setOptionalTypeInfoField(Optional.of(typeInfoBean));
    bean.setOptionalTypeInfoGetter(Optional.of(typeInfoBean));
    bean.setOptionalTypeInfoSetter(Optional.of(typeInfoBean));
    bean.setTypeInfoField(typeInfoBean);
    bean.setTypeInfoGetter(typeInfoBean);
    bean.setTypeInfoSetter(typeInfoBean);

    String json = "{\"stringProperty\":\"value\"}";
    List<Byte> bytes = toList(json.getBytes(StandardCharsets.UTF_8));

    assertThat(bind(bean)).isEqualTo(map(
            "annotatedField", json,
            "annotatedGetter", json,
            "annotatedSetter", json,
            "annotatedFieldWithDefault", json,
            "annotatedGetterWithDefault", json,
            "annotatedSetterWithDefault", json,
            "optionalField", json,
            "optionalGetter", json,
            "optionalSetter", json,
            "binaryField", bytes,
            "binaryFieldWithDefault", bytes,
            "jsonNodeField", json,
            "optionalTypeInfoField", typeInfoBean,
            "optionalTypeInfoGetter", typeInfoBean,
            "optionalTypeInfoSetter", typeInfoBean,
            "typeInfoField", typeInfoBean,
            "typeInfoGetter", typeInfoBean,
            "typeInfoSetter", typeInfoBean
    ));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map(
            "prefix.annotatedField", json,
            "prefix.annotatedGetter", json,
            "prefix.annotatedSetter", json,
            "prefix.annotatedFieldWithDefault", json,
            "prefix.annotatedGetterWithDefault", json,
            "prefix.annotatedSetterWithDefault", json,
            "prefix.optionalField", json,
            "prefix.optionalGetter", json,
            "prefix.optionalSetter", json,
            "prefix.binaryField", bytes,
            "prefix.binaryFieldWithDefault", bytes,
            "prefix.jsonNodeField", json,
            "prefix.optionalTypeInfoField", typeInfoBean,
            "prefix.optionalTypeInfoGetter", typeInfoBean,
            "prefix.optionalTypeInfoSetter", typeInfoBean,
            "prefix.typeInfoField", typeInfoBean,
            "prefix.typeInfoGetter", typeInfoBean,
            "prefix.typeInfoSetter", typeInfoBean
    ));
  }

  @Test
  public void itBindsNullStoredAsJsonBeanCorrectly() {
    StoredAsJsonBean bean = new StoredAsJsonBean();

    String json = "{\"stringProperty\":\"value\"}";
    List<Byte> bytes = toList(json.getBytes(StandardCharsets.UTF_8));

    assertThat(bind(bean)).isEqualTo(map(
            "annotatedField", null,
            "annotatedGetter", null,
            "annotatedSetter", null,
            "annotatedFieldWithDefault", json,
            "annotatedGetterWithDefault", json,
            "annotatedSetterWithDefault", json,
            "optionalField", null,
            "optionalGetter", null,
            "optionalSetter", null,
            "binaryField", null,
            "binaryFieldWithDefault", bytes,
            "jsonNodeField", null,
            "optionalTypeInfoField", null,
            "optionalTypeInfoGetter", null,
            "optionalTypeInfoSetter", null,
            "typeInfoField", null,
            "typeInfoGetter", null,
            "typeInfoSetter", null
    ));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map(
            "prefix.annotatedField", null,
            "prefix.annotatedGetter", null,
            "prefix.annotatedSetter", null,
            "prefix.annotatedFieldWithDefault", json,
            "prefix.annotatedGetterWithDefault", json,
            "prefix.annotatedSetterWithDefault", json,
            "prefix.optionalField", null,
            "prefix.optionalGetter", null,
            "prefix.optionalSetter", null,
            "prefix.binaryField", null,
            "prefix.binaryFieldWithDefault", bytes,
            "prefix.jsonNodeField", null,
            "prefix.optionalTypeInfoField", null,
            "prefix.optionalTypeInfoGetter", null,
            "prefix.optionalTypeInfoSetter", null,
            "prefix.typeInfoField", null,
            "prefix.typeInfoGetter", null,
            "prefix.typeInfoSetter", null
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

  @Test
  public void itBindsListFieldsCorrectly() {
    ListBean bean = new ListBean();
    List<Integer> values = Arrays.asList(1, 2, 3);
    bean.setValues(values);

    assertThat(bind(bean)).isEqualTo(map("values", values));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map("prefix.values", values));
  }

  @Test
  public void itUsesServiceLoaderToDiscoverModules() {
    ServiceLoaderBean bean = new ServiceLoaderBean();
    bean.setId(50);
    bean.setName("test");

    assertThat(bind(bean)).isEqualTo(map("id_value", 50, "name_value", "test"));
    assertThat(bindWithPrefix("prefix", bean)).isEqualTo(map("prefix.id_value", 50, "prefix.name_value", "test"));
  }

  private static Map<String, Object> map(Object... strings) {
    Map<String, Object> map = new HashMap<String, Object>();
    for (int i = 0; i < strings.length; i += 2) {
      map.put((String) strings[i], strings[i + 1]);
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
      if (value instanceof byte[]) {
        value = toList((byte[]) value);
      }

      bindings.put(key, value);
    }

    public Map<String, Object> getBindings() {
      return bindings;
    }
  }

  private static List<Byte> toList(byte[] bytes) {
    List<Byte> byteList = new ArrayList<>();
    for (int i = 0; i < bytes.length; i++) {
      byteList.add(bytes[i]);
    }

    return byteList;
  }
}
