package com.hubspot.rosetta.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Optional;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.InnerBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class StoredAsJsonTest {
  private StoredAsJsonBean bean;
  private InnerBean inner;
  private final JsonNode innerJsonNode = Rosetta.getMapper().createObjectNode().set("stringProperty", TextNode.valueOf("value"));
  private final JsonNode expected = TextNode.valueOf("{\"stringProperty\":\"value\"}");
  private final JsonNode expectedBinary = BinaryNode.valueOf(expected.textValue().getBytes(StandardCharsets.UTF_8));

  @Before
  public void setup() {
    bean = new StoredAsJsonBean();
    inner = new InnerBean();
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

  @Test
  public void testOptionalFieldSerialization() {
    bean.setOptionalField(Optional.of(inner));

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalField")).isEqualTo(expected);
  }

  @Test
  public void testOptionalFieldDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalField", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalField().isPresent()).isTrue();
    assertThat(bean.getOptionalField().get().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testOptionalFieldNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalField")).isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testOptionalFieldNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalField", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalField()).isNotNull();
    assertThat(bean.getOptionalField().isPresent()).isFalse();
  }

  @Test
  public void testOptionalGetterSerialization() {
    bean.setOptionalGetter(Optional.of(inner));

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalGetter")).isEqualTo(expected);
  }

  @Test
  public void testOptionalGetterDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalGetter", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalGetter().isPresent()).isTrue();
    assertThat(bean.getOptionalGetter().get().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testOptionalGetterNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalGetter")).isEqualTo(NullNode.getInstance());

  }

  @Test
  public void testOptionalGetterNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalGetter", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalGetter()).isNotNull();
    assertThat(bean.getOptionalGetter().isPresent()).isFalse();
  }

  @Test
  public void testOptionalSetterSerialization() {
    bean.setOptionalSetter(Optional.of(inner));

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalSetter")).isEqualTo(expected);
  }

  @Test
  public void testOptionalSetterDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalSetter", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalSetter().isPresent()).isTrue();
    assertThat(bean.getOptionalSetter().get().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testOptionalSetterNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalSetter")).isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testOptionalSetterNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalSetter", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalSetter()).isNotNull();
    assertThat(bean.getOptionalSetter().isPresent()).isFalse();
  }

  @Test
  public void testBinaryFieldSerialization() {
    bean.setBinaryField(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryField")).isEqualTo(expectedBinary);
  }

  @Test
  public void testBinaryFieldDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("binaryField", expectedBinary);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getBinaryField().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testBinaryFieldNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryField")).isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testBinaryFieldNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("binaryField", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getBinaryField()).isNull();
  }

  @Test
  public void testBinaryFieldWithDefaultSerialization() {
    bean.setBinaryFieldWithDefault(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryFieldWithDefault")).isEqualTo(expectedBinary);
  }

  @Test
  public void testBinaryFieldWithDefaultDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("binaryFieldWithDefault", expectedBinary);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getBinaryFieldWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testBinaryFieldWithDefaultNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryFieldWithDefault")).isEqualTo(expectedBinary);
  }

  @Test
  public void testBinaryFieldWithDefaultNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("binaryFieldWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getBinaryFieldWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testJsonNodeFieldSerialization() {
    bean.setJsonNodeField(innerJsonNode);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("jsonNodeField")).isEqualTo(expected);
  }

  @Test
  public void testJsonNodeFieldDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("jsonNodeField", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getJsonNodeField()).isEqualTo(innerJsonNode);
  }

  @Test
  public void testJsonNodeNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("jsonNodeField")).isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testJsonNodeNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("jsonNodeField", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getJsonNodeField()).isEqualTo(NullNode.getInstance());
  }
}
