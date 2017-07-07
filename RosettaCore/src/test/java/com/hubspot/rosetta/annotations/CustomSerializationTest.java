package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.ImmutableList;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.CustomSerializationBean;

public class CustomSerializationTest {
  private CustomSerializationBean bean;
  private List<String> stringList;
  private final JsonNode expected = TextNode.valueOf("one;two;buckle;my;shoe");

  @Before
  public void setup() {
    bean = new CustomSerializationBean();
    stringList = ImmutableList.of("one", "two", "buckle", "my", "shoe");
  }

  @Test
  public void testAnnotatedFieldSerialization() {
    bean.setAnnotatedField(stringList);
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedField"))
        .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedFieldDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.set("annotatedField", expected);

    CustomSerializationBean bean = Rosetta.getMapper().treeToValue(node, CustomSerializationBean.class);
    assertThat(bean.getAnnotatedField()).containsExactlyElementsOf(stringList);
  }

  @Test
  public void testAnnotatedFieldNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedField"))
        .isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testAnnotatedFieldNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.set("annotatedField", NullNode.getInstance());

    CustomSerializationBean bean = Rosetta.getMapper().treeToValue(node, CustomSerializationBean.class);
    assertThat(bean.getAnnotatedField())
        .isNull();
  }

  @Test
  public void testAnnotatedGetterSerialization() {
    bean.setAnnotatedGetter(stringList);
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetter"))
        .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedGetterDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.set("annotatedGetter", expected);

    CustomSerializationBean bean = Rosetta.getMapper().treeToValue(node, CustomSerializationBean.class);
    assertThat(bean.getAnnotatedGetter()).containsExactlyElementsOf(stringList);
  }

  @Test
  public void testAnnotatedGetterNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetter"))
        .isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testAnnotatedGetterNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.set("annotatedGetter", NullNode.getInstance());

    CustomSerializationBean bean = Rosetta.getMapper().treeToValue(node, CustomSerializationBean.class);
    assertThat(bean.getAnnotatedField())
        .isNull();
  }

  @Test
  public void testAnnotatedSetterSerialization() {
    bean.setAnnotatedSetter(stringList);
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetter"))
        .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedSetterDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.set("annotatedSetter", expected);

    CustomSerializationBean bean = Rosetta.getMapper().treeToValue(node, CustomSerializationBean.class);
    assertThat(bean.getAnnotatedSetter()).containsExactlyElementsOf(stringList);
  }

  @Test
  public void testAnnotatedSetterNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetter"))
        .isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testAnnotatedSetterNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.set("annotatedSetter", NullNode.getInstance());

    CustomSerializationBean bean = Rosetta.getMapper().treeToValue(node, CustomSerializationBean.class);
    assertThat(bean.getAnnotatedField())
        .isNull();
  }
}
