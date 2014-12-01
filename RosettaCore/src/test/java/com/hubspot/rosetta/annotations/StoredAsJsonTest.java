package com.hubspot.rosetta.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean.Inner;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StoredAsJsonTest {
  private StoredAsJsonBean bean;
  private Inner inner;
  private final JsonNode expected = TextNode.valueOf("{\"stringProperty\":\"value\"}");

  @Before
  public void setup() {
    bean = new StoredAsJsonBean();
    inner = new Inner();
    inner.setStringProperty("value");
  }

  @Test
  public void testAnnotatedFieldSerialization() {
    bean.setAnnotatedField(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedField")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedFieldDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedField", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedField().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedFieldNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedField")).isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testAnnotatedFieldNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedField", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedField()).isNull();
  }

  @Test
  public void testAnnotatedGetterSerialization() {
    bean.setAnnotatedGetter(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetter")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedGetterDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedGetter", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedGetter().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedGetterNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetter")).isEqualTo(NullNode.getInstance());

  }

  @Test
  public void testAnnotatedGetterNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedGetter", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedGetter()).isNull();
  }

  @Test
  public void testAnnotatedSetterSerialization() {
    bean.setAnnotatedSetter(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetter")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedSetterDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedSetter", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedSetter().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedSetterNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetter")).isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testAnnotatedSetterNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedSetter", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedSetter()).isNull();
  }

  @Test
  public void testAnnotatedFieldWithDefaultSerialization() {
    bean.setAnnotatedFieldWithDefault(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedFieldWithDefault")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedFieldWithDefaultDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedFieldWithDefault", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedFieldWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedFieldWithDefaultNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedFieldWithDefault")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedFieldWithDefaultNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedFieldWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedFieldWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedGetterWithDefaultSerialization() {
    bean.setAnnotatedGetterWithDefault(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetterWithDefault")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedGetterWithDefaultDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedGetterWithDefault", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedGetterWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedGetterWithDefaultNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetterWithDefault")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedGetterWithDefaultNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedGetterWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedGetterWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedSetterWithDefaultSerialization() {
    bean.setAnnotatedSetterWithDefault(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetterWithDefault")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedSetterWithDefaultDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedSetterWithDefault", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedSetterWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testAnnotatedSetterWithDefaultNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetterWithDefault")).isEqualTo(expected);
  }

  @Test
  public void testAnnotatedSetterWithDefaultNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedSetterWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedSetterWithDefault().getStringProperty()).isEqualTo("value");
  }
}
