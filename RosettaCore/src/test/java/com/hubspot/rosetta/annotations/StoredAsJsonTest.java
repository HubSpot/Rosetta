package com.hubspot.rosetta.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.beans.FieldBeanStoredAsJson;
import com.hubspot.rosetta.beans.InnerBean;
import com.hubspot.rosetta.beans.ListStoredAsJsonBeanIF.ListStoredAsJsonBean;
import com.hubspot.rosetta.beans.MapStoredAsJsonBean;
import com.hubspot.rosetta.beans.NestedStoredAsJsonBean;
import com.hubspot.rosetta.beans.NullPolymorphicBean;
import com.hubspot.rosetta.beans.OptionalStoredAsJsonBeanIF.OptionalStoredAsJsonBean;
import com.hubspot.rosetta.beans.OptionalStoredAsJsonTypeInfoBean;
import com.hubspot.rosetta.beans.OptionalStoredAsJsonTypeInfoBean.Polymorph;
import com.hubspot.rosetta.beans.PolymorphicBeanA;
import com.hubspot.rosetta.beans.PolymorphicBeanASubTypeA;
import com.hubspot.rosetta.beans.PolymorphicStoredAsJsonBean;
import com.hubspot.rosetta.beans.SetStoredAsJsonBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import com.hubspot.rosetta.beans.StoredAsJsonListTypeInfoBean;
import com.hubspot.rosetta.beans.StoredAsJsonListTypeInfoBean.ConcreteStoredAsJsonList;
import com.hubspot.rosetta.beans.StoredAsJsonTypeInfoBean;
import com.hubspot.rosetta.beans.StoredAsJsonTypeInfoBean.ConcreteStoredAsJsonTypeInfo;
import com.hubspot.rosetta.internal.RosettaModule;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class StoredAsJsonTest {

  private StoredAsJsonBean bean;
  private InnerBean inner;
  private final JsonNode innerJsonNode = Rosetta
    .getMapper()
    .createObjectNode()
    .set("stringProperty", TextNode.valueOf("value"));
  private final JsonNode expected = TextNode.valueOf("{\"stringProperty\":\"value\"}");
  private final JsonNode expectedBinary = BinaryNode.valueOf(
    expected.textValue().getBytes(StandardCharsets.UTF_8)
  );

  private StoredAsJsonTypeInfoBean typeInfoBean;
  private final JsonNode expectedTypeInfo = TextNode.valueOf(
    "{\"concreteValue\":\"internal\",\"generalValue\":\"General\",\"type\":\"concrete\"}"
  );

  @Before
  public void setup() {
    bean = new StoredAsJsonBean();
    inner = new InnerBean();
    inner.setStringProperty("value");

    ConcreteStoredAsJsonTypeInfo concrete = new ConcreteStoredAsJsonTypeInfo();
    concrete.setGeneralValue("General");
    concrete.setConcreteValue("internal");
    typeInfoBean = concrete;
  }

  @Test
  public void testAnnotatedFieldSerialization() {
    bean.setAnnotatedField(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedField"))
      .isEqualTo(expected);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedField"))
      .isEqualTo(NullNode.getInstance());
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

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetter"))
      .isEqualTo(expected);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetter"))
      .isEqualTo(NullNode.getInstance());
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

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetter"))
      .isEqualTo(expected);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetter"))
      .isEqualTo(NullNode.getInstance());
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

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedFieldWithDefault"))
      .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedFieldWithDefaultDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedFieldWithDefault", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedFieldWithDefault().getStringProperty())
      .isEqualTo("value");
  }

  @Test
  public void testAnnotatedFieldWithDefaultNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedFieldWithDefault"))
      .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedFieldWithDefaultNullDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedFieldWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedFieldWithDefault().getStringProperty())
      .isEqualTo("value");
  }

  @Test
  public void testAnnotatedGetterWithDefaultSerialization() {
    bean.setAnnotatedGetterWithDefault(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetterWithDefault"))
      .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedGetterWithDefaultDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedGetterWithDefault", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedGetterWithDefault().getStringProperty())
      .isEqualTo("value");
  }

  @Test
  public void testAnnotatedGetterWithDefaultNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedGetterWithDefault"))
      .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedGetterWithDefaultNullDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedGetterWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedGetterWithDefault().getStringProperty())
      .isEqualTo("value");
  }

  @Test
  public void testAnnotatedSetterWithDefaultSerialization() {
    bean.setAnnotatedSetterWithDefault(inner);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetterWithDefault"))
      .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedSetterWithDefaultDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedSetterWithDefault", expected);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedSetterWithDefault().getStringProperty())
      .isEqualTo("value");
  }

  @Test
  public void testAnnotatedSetterWithDefaultNullSerialization() {
    assertThat(Rosetta.getMapper().valueToTree(bean).get("annotatedSetterWithDefault"))
      .isEqualTo(expected);
  }

  @Test
  public void testAnnotatedSetterWithDefaultNullDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("annotatedSetterWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getAnnotatedSetterWithDefault().getStringProperty())
      .isEqualTo("value");
  }

  @Test
  public void testOptionalFieldSerialization() {
    bean.setOptionalField(Optional.of(inner));

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalField"))
      .isEqualTo(expected);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalField"))
      .isEqualTo(NullNode.getInstance());
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

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalGetter"))
      .isEqualTo(expected);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalGetter"))
      .isEqualTo(NullNode.getInstance());
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

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalSetter"))
      .isEqualTo(expected);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalSetter"))
      .isEqualTo(NullNode.getInstance());
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

    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryField"))
      .isEqualTo(expectedBinary);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryField"))
      .isEqualTo(NullNode.getInstance());
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

    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryFieldWithDefault"))
      .isEqualTo(expectedBinary);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("binaryFieldWithDefault"))
      .isEqualTo(expectedBinary);
  }

  @Test
  public void testBinaryFieldWithDefaultNullDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("binaryFieldWithDefault", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getBinaryFieldWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void testJsonNodeFieldSerialization() {
    bean.setJsonNodeField(innerJsonNode);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("jsonNodeField"))
      .isEqualTo(expected);
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
    assertThat(Rosetta.getMapper().valueToTree(bean).get("jsonNodeField"))
      .isEqualTo(NullNode.getInstance());
  }

  @Test
  public void testJsonNodeNullDeserialization() throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("jsonNodeField", NullNode.getInstance());

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getJsonNodeField()).isEqualTo(NullNode.getInstance());
  }

  @Test
  public void itHandlesSubTypes() throws JsonProcessingException {
    ConcreteStoredAsJsonList typeInfoBean = new ConcreteStoredAsJsonList();
    typeInfoBean.setInnerBeans(Collections.singletonList(inner));

    TextNode expectedList = TextNode.valueOf("[{\"stringProperty\":\"value\"}]");
    JsonNode node = Rosetta.getMapper().valueToTree(typeInfoBean);
    assertThat(node.get("innerBeans")).isEqualTo(expectedList);

    assertThat(
      Rosetta
        .getMapper()
        .treeToValue(node, StoredAsJsonListTypeInfoBean.class)
        .getInnerBeans()
        .get(0)
        .getStringProperty()
    )
      .isEqualTo("value");

    assertThat(
      Rosetta
        .getMapper()
        .treeToValue(node, ConcreteStoredAsJsonList.class)
        .getInnerBeans()
        .get(0)
        .getStringProperty()
    )
      .isEqualTo("value");
  }

  @Test
  public void itHandlesAnnotatedOptionalGenericFieldSerialization() {
    bean.setOptionalTypeInfoField(Optional.of(typeInfoBean));

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalTypeInfoField"))
      .isEqualTo(expectedTypeInfo);
  }

  @Test
  public void itHandlesAnnotatedOptionalGenericFieldDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalTypeInfoField", expectedTypeInfo);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalTypeInfoField().isPresent()).isTrue();
    assertThat(bean.getOptionalTypeInfoField().get())
      .isInstanceOf(ConcreteStoredAsJsonTypeInfo.class);
    assertThat(bean.getOptionalTypeInfoField().get().getGeneralValue())
      .isEqualTo("General");
    assertThat(
      ((ConcreteStoredAsJsonTypeInfo) bean
          .getOptionalTypeInfoField()
          .get()).getConcreteValue()
    )
      .isEqualTo("internal");
  }

  @Test
  public void itHandlesAnnotatedOptionalGenericGetterSerialization() {
    bean.setOptionalTypeInfoGetter(Optional.of(typeInfoBean));

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalTypeInfoGetter"))
      .isEqualTo(expectedTypeInfo);
  }

  @Test
  public void itHandlesAnnotatedOptionalGenericGetterDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalTypeInfoGetter", expectedTypeInfo);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalTypeInfoGetter().isPresent()).isTrue();
    assertThat(bean.getOptionalTypeInfoGetter().get())
      .isInstanceOf(ConcreteStoredAsJsonTypeInfo.class);
    assertThat(bean.getOptionalTypeInfoGetter().get().getGeneralValue())
      .isEqualTo("General");
    assertThat(
      ((ConcreteStoredAsJsonTypeInfo) bean
          .getOptionalTypeInfoGetter()
          .get()).getConcreteValue()
    )
      .isEqualTo("internal");
  }

  @Test
  public void itHandlesAnnotatedOptionalGenericSetterSerialization() {
    bean.setOptionalTypeInfoSetter(Optional.of(typeInfoBean));

    assertThat(Rosetta.getMapper().valueToTree(bean).get("optionalTypeInfoSetter"))
      .isEqualTo(expectedTypeInfo);
  }

  @Test
  public void itHandlesAnnotatedOptionalGenericSetterDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("optionalTypeInfoSetter", expectedTypeInfo);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getOptionalTypeInfoSetter().isPresent()).isTrue();
    assertThat(bean.getOptionalTypeInfoSetter().get())
      .isInstanceOf(ConcreteStoredAsJsonTypeInfo.class);
    assertThat(bean.getOptionalTypeInfoSetter().get().getGeneralValue())
      .isEqualTo("General");
    assertThat(
      ((ConcreteStoredAsJsonTypeInfo) bean
          .getOptionalTypeInfoSetter()
          .get()).getConcreteValue()
    )
      .isEqualTo("internal");
  }

  @Test
  public void itHandlesAnnotatedGenericFieldSerialization() {
    bean.setTypeInfoField(typeInfoBean);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("typeInfoField"))
      .isEqualTo(expectedTypeInfo);
  }

  @Test
  public void itHandlesAnnotatedGenericFieldDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("typeInfoField", expectedTypeInfo);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getTypeInfoField()).isNotNull();
    assertThat(bean.getTypeInfoField()).isInstanceOf(ConcreteStoredAsJsonTypeInfo.class);
    assertThat(bean.getTypeInfoField().getGeneralValue()).isEqualTo("General");
    assertThat(
      ((ConcreteStoredAsJsonTypeInfo) bean.getTypeInfoField()).getConcreteValue()
    )
      .isEqualTo("internal");
  }

  @Test
  public void itHandlesAnnotatedGenericGetterSerialization() {
    bean.setTypeInfoGetter(typeInfoBean);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("typeInfoGetter"))
      .isEqualTo(expectedTypeInfo);
  }

  @Test
  public void itHandlesAnnotatedGenericGetterDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("typeInfoGetter", expectedTypeInfo);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getTypeInfoGetter()).isNotNull();
    assertThat(bean.getTypeInfoGetter()).isInstanceOf(ConcreteStoredAsJsonTypeInfo.class);
    assertThat(bean.getTypeInfoGetter().getGeneralValue()).isEqualTo("General");
    assertThat(
      ((ConcreteStoredAsJsonTypeInfo) bean.getTypeInfoGetter()).getConcreteValue()
    )
      .isEqualTo("internal");
  }

  @Test
  public void itHandlesAnnotatedGenericSetterSerialization() {
    bean.setTypeInfoSetter(typeInfoBean);

    assertThat(Rosetta.getMapper().valueToTree(bean).get("typeInfoSetter"))
      .isEqualTo(expectedTypeInfo);
  }

  @Test
  public void itHandlesAnnotatedGenericSetterDeserialization()
    throws JsonProcessingException {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.put("typeInfoSetter", expectedTypeInfo);

    StoredAsJsonBean bean = Rosetta.getMapper().treeToValue(node, StoredAsJsonBean.class);
    assertThat(bean.getTypeInfoSetter()).isNotNull();
    assertThat(bean.getTypeInfoSetter()).isInstanceOf(ConcreteStoredAsJsonTypeInfo.class);
    assertThat(bean.getTypeInfoSetter().getGeneralValue()).isEqualTo("General");
    assertThat(
      ((ConcreteStoredAsJsonTypeInfo) bean.getTypeInfoSetter()).getConcreteValue()
    )
      .isEqualTo("internal");
  }

  @Test
  @Ignore
  public void testNestedStoredAsJsonBeans() throws JsonProcessingException {
    InnerBean innerBean = new InnerBean();
    innerBean.setStringProperty("value");

    StoredAsJsonBean storedAsJsonBean = new StoredAsJsonBean();
    storedAsJsonBean.setAnnotatedField(innerBean);

    NestedStoredAsJsonBean top = new NestedStoredAsJsonBean();
    top.setAnnotatedField(storedAsJsonBean);

    JsonNode node = Rosetta.getMapper().valueToTree(top);
    assertThat(Rosetta.getMapper().treeToValue(node, NestedStoredAsJsonBean.class))
      .isEqualTo(top);
  }

  @Test
  public void testPolymorphicStoredAsJsonBeans() throws JsonProcessingException {
    PolymorphicStoredAsJsonBean bean = new PolymorphicStoredAsJsonBean();
    bean.setAnnotatedField(new PolymorphicBeanA());

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    assertThat(node.get("annotatedField")).isNotNull();
    assertThat(node.get("annotatedField").hasNonNull("beanType"));

    assertThat(
      Rosetta
        .getMapper()
        .treeToValue(node, PolymorphicStoredAsJsonBean.class)
        .getAnnotatedField()
    )
      .isInstanceOf(PolymorphicBeanA.class);
  }

  @Test
  public void testNullPolymorphicStoredAsJsonBean() {
    PolymorphicStoredAsJsonBean bean = new PolymorphicStoredAsJsonBean();
    bean.setAnnotatedField(new NullPolymorphicBean());

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    assertThat(node.get("annotatedField")).isNotNull();
    assertThat(node.get("annotatedField").isNull()).isTrue();
  }

  @Test
  public void testSerializingOptionalStoredAsJsonBean() {
    OptionalStoredAsJsonBean bean = new OptionalStoredAsJsonBean(
      java.util.Optional.of(new PolymorphicBeanA())
    );

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    assertThat(node.get("bean")).isNotNull();
    assertThat(node.get("bean").textValue()).contains("beanType");
  }

  @Test
  public void testSerializingOptionalStoredAsJsonSubtypeBean() {
    OptionalStoredAsJsonBean bean = new OptionalStoredAsJsonBean(
      java.util.Optional.of(new PolymorphicBeanASubTypeA())
    );

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    assertThat(node.get("bean")).isNotNull();
    assertThat(node.get("bean").textValue()).contains("beanType");
  }

  @Test
  public void testSerializingListStoredAsJsonBean() {
    ListStoredAsJsonBean bean = new ListStoredAsJsonBean(
      Arrays.asList(new PolymorphicBeanA())
    );

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    assertThat(node.get("beans")).isNotNull();
    assertThat(node.get("beans").textValue()).contains("beanType");
  }

  @Test
  public void testSerializingMapStoredAsJsonBean() {
    MapStoredAsJsonBean bean = new MapStoredAsJsonBean(
      ImmutableMap.of("A", new PolymorphicBeanA())
    );

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    assertThat(node.get("beans")).isNotNull();
    assertThat(node.get("beans").textValue()).contains("beanType");
  }

  @Test
  public void testSerializingSetStoredAsJsonBean() {
    SetStoredAsJsonBean bean = new SetStoredAsJsonBean(
      ImmutableSet.of(new PolymorphicBeanA())
    );

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    assertThat(node.get("beans")).isNotNull();
    assertThat(node.get("beans").textValue()).contains("beanType");
  }

  @Test
  public void testSerializingOptionalStoredAsJsonTypeInfoBean() throws Exception {
    Polymorph polymorph = () -> "lambda";
    OptionalStoredAsJsonTypeInfoBean bean = new OptionalStoredAsJsonTypeInfoBean(
      polymorph
    );

    String actual = Rosetta
      .getMapper()
      .valueToTree(bean)
      .get("polymorphicField")
      .textValue();
    String expected = Rosetta.getMapper().valueToTree(polymorph).toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDeserializingStoredAsJsonPrivateField() throws Exception {
    ObjectNode node = Rosetta.getMapper().createObjectNode();
    node.putObject("bean").put("beanType", "C").put("value", "test");

    FieldBeanStoredAsJson res = Rosetta
      .getMapper()
      .readValue(node.toString(), FieldBeanStoredAsJson.class);
  }

  @Test
  public void itIncludesAllFieldsByDefaultWithRosettaMapper()
    throws JsonProcessingException {
    // Verify that the default Rosetta.getMapper() still includes all fields
    // including nulls and empty collections (standard DAO behavior)
    InnerBeanWithList bean = new InnerBeanWithList();
    bean.values = Collections.emptyList();
    bean.name = null;

    String json = Rosetta.getMapper().writeValueAsString(bean);

    // Both fields should be present even though one is null and one is empty
    assertThat(json).contains("\"name\":null");
    assertThat(json).contains("\"values\":[]");
  }

  @Test
  public void itRespectsMapperLevelInclusionForStoredAsJsonFields()
    throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper()
      .registerModule(new RosettaModule())
      .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    BeanWithListStoredAsJsonNoAnnotation bean =
      new BeanWithListStoredAsJsonNoAnnotation();
    bean.inner = new InnerBeanWithList();
    bean.inner.values = Collections.emptyList();
    bean.inner.name = "test";

    JsonNode node = mapper.valueToTree(bean);

    String innerJson = node.get("inner").textValue();
    assertThat(mapper.readTree(innerJson))
      .isEqualTo(mapper.readTree("{\"name\": \"test\"}"));
  }

  @Test
  public void itRespectsJsonIncludeAnnotationOnStoredAsJsonField()
    throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper().registerModule(new RosettaModule());

    BeanWithListStoredAsJson bean = new BeanWithListStoredAsJson();
    bean.inner = new InnerBeanWithList();
    bean.inner.values = Collections.emptyList();
    bean.inner.name = "test";

    JsonNode node = mapper.valueToTree(bean);

    assertThat(node.get("inner").isTextual()).isTrue();
    String innerJson = node.get("inner").textValue();
    assertThat(innerJson).contains("\"name\":\"test\"");
    assertThat(innerJson)
      .as("@JsonInclude(NON_EMPTY) should exclude empty list in @StoredAsJson field")
      .doesNotContain("values");
  }

  @Test
  public void itSerializesRosettaValueEnumsCorrectlyInStoredAsJson() throws Exception {
    InnerBeanWithEnum inner = new InnerBeanWithEnum();
    inner.type = TestEnumWithRosettaValue.B;
    inner.name = "test";

    BeanWithEnumStoredAsJson bean = new BeanWithEnumStoredAsJson();
    bean.inner = inner;

    JsonNode node = Rosetta.getMapper().valueToTree(bean);
    String innerJson = node.get("inner").textValue();
    JsonNode innerNode = Rosetta.getMapper().readTree(innerJson);

    assertThat(innerNode.get("type").isNumber()).isTrue();
    assertThat(innerNode.get("type").intValue()).isEqualTo(1);
  }

  @Test
  public void itDeserializesRosettaCreatorEnumsCorrectlyInStoredAsJson()
    throws Exception {
    ObjectMapper mapper = Rosetta.getMapper();

    ObjectNode outerNode = mapper.createObjectNode();
    outerNode.put("inner", "{\"type\":1,\"name\":\"test\"}");

    BeanWithEnumStoredAsJson result = mapper.convertValue(
      outerNode,
      BeanWithEnumStoredAsJson.class
    );

    assertThat(result.inner.type).isEqualTo(TestEnumWithRosettaValue.B);
    assertThat(result.inner.name).isEqualTo("test");
  }

  public enum TestEnumWithRosettaValue {
    A(0),
    B(1);

    private final int value;

    TestEnumWithRosettaValue(int v) {
      this.value = v;
    }

    @RosettaValue
    public int getValue() {
      return value;
    }

    @RosettaCreator
    public static TestEnumWithRosettaValue fromValue(int v) {
      for (TestEnumWithRosettaValue e : values()) {
        if (e.value == v) return e;
      }
      throw new IllegalArgumentException("Unknown value: " + v);
    }
  }

  public static class BeanWithEnumStoredAsJson {

    @StoredAsJson
    public InnerBeanWithEnum inner;
  }

  public static class InnerBeanWithEnum {

    public TestEnumWithRosettaValue type;
    public String name;
  }

  public static class BeanWithListStoredAsJson {

    @StoredAsJson
    public InnerBeanWithList inner;
  }

  public static class BeanWithListStoredAsJsonNoAnnotation {

    @StoredAsJson
    public InnerBeanWithList inner;
  }

  public static class InnerBeanWithList {

    public String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<String> values;
  }
}
