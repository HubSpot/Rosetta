package com.hubspot.rosetta.immutables;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.hubspot.immutables.utils.WireSafeEnum;
import com.hubspot.rosetta.immutables.beans.CustomEnum;
import com.hubspot.rosetta.immutables.beans.SimpleEnum;
import com.hubspot.rosetta.immutables.beans.WireSafeEnumStoredAsJsonBean;
import com.hubspot.rosetta.immutables.beans.WireSafeEnumStoredAsJsonBean.InnerWireSafeEnumBean;
import com.hubspot.rosetta.internal.RosettaModule;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class WireSafeEnumStoredAsJsonTest {

  private static final ObjectMapper MAPPER = new ObjectMapper()
    .registerModules(
      new Jdk8Module(),
      new RosettaImmutablesModule(),
      new RosettaModule()
    );

  @Test
  public void itSerializesWireSafeEnumWithRosettaValueInStoredAsJson() throws Exception {
    InnerWireSafeEnumBean inner = new InnerWireSafeEnumBean();
    inner.setCustomEnum(WireSafeEnum.of(CustomEnum.TWO));
    inner.setSimpleEnum(WireSafeEnum.of(SimpleEnum.ONE));
    inner.setName("test");

    WireSafeEnumStoredAsJsonBean bean = new WireSafeEnumStoredAsJsonBean();
    bean.setInner(inner);

    JsonNode node = MAPPER.valueToTree(bean);
    String innerJson = node.get("inner").textValue();
    JsonNode innerNode = MAPPER.readTree(innerJson);

    assertThat(innerNode.get("customEnum").isNumber()).isTrue();
    assertThat(innerNode.get("customEnum").intValue()).isEqualTo(2);
    assertThat(innerNode.get("simpleEnum").isTextual()).isTrue();
    assertThat(innerNode.get("simpleEnum").textValue()).isEqualTo("ONE");
    assertThat(innerNode.get("name").textValue()).isEqualTo("test");
  }

  @Test
  public void itRoundTripsWireSafeEnumWithRosettaValueThroughStoredAsJson()
    throws Exception {
    InnerWireSafeEnumBean inner = new InnerWireSafeEnumBean();
    inner.setCustomEnum(WireSafeEnum.of(CustomEnum.ONE));
    inner.setSimpleEnum(WireSafeEnum.of(SimpleEnum.TWO));
    inner.setName("roundtrip");

    WireSafeEnumStoredAsJsonBean bean = new WireSafeEnumStoredAsJsonBean();
    bean.setInner(inner);

    JsonNode node = MAPPER.valueToTree(bean);

    WireSafeEnumStoredAsJsonBean result = MAPPER.treeToValue(
      node,
      WireSafeEnumStoredAsJsonBean.class
    );

    assertThat(result.getInner().getCustomEnum())
      .isEqualTo(WireSafeEnum.of(CustomEnum.ONE));
    assertThat(result.getInner().getSimpleEnum())
      .isEqualTo(WireSafeEnum.of(SimpleEnum.TWO));
    assertThat(result.getInner().getName()).isEqualTo("roundtrip");
  }

  @Test
  public void itDeserializesWireSafeEnumWithRosettaValueFromStoredAsJson()
    throws Exception {
    ObjectNode outerNode = MAPPER.createObjectNode();
    outerNode.put(
      "inner",
      "{\"customEnum\":1,\"simpleEnum\":\"ONE\",\"name\":\"deser\"}"
    );

    WireSafeEnumStoredAsJsonBean result = MAPPER.treeToValue(
      outerNode,
      WireSafeEnumStoredAsJsonBean.class
    );

    assertThat(result.getInner().getCustomEnum())
      .isEqualTo(WireSafeEnum.of(CustomEnum.ONE));
    assertThat(result.getInner().getSimpleEnum())
      .isEqualTo(WireSafeEnum.of(SimpleEnum.ONE));
    assertThat(result.getInner().getName()).isEqualTo("deser");
  }

  @Test
  public void itDeserializesWireSafeEnumViaConvertValueWithCustomMapperCopy() {
    ObjectMapper base = new ObjectMapper()
      .registerModules(new Jdk8Module(), new RosettaModule());

    ObjectMapper customCopy = base.copy().registerModule(new RosettaImmutablesModule());

    Map<String, Object> rowMap = new HashMap<>();
    rowMap.put("inner", "{\"customEnum\":1,\"simpleEnum\":\"ONE\",\"name\":\"test\"}");

    WireSafeEnumStoredAsJsonBean result = customCopy.convertValue(
      rowMap,
      WireSafeEnumStoredAsJsonBean.class
    );

    assertThat(result.getInner().getCustomEnum())
      .isEqualTo(WireSafeEnum.of(CustomEnum.ONE));
    assertThat(result.getInner().getSimpleEnum())
      .isEqualTo(WireSafeEnum.of(SimpleEnum.ONE));
    assertThat(result.getInner().getName()).isEqualTo("test");
  }

  @Test
  public void itSerializesWireSafeEnumViaValueToTreeWithCustomMapperCopy()
    throws Exception {
    ObjectMapper base = new ObjectMapper()
      .registerModules(new Jdk8Module(), new RosettaModule());

    ObjectMapper customCopy = base.copy().registerModule(new RosettaImmutablesModule());

    InnerWireSafeEnumBean inner = new InnerWireSafeEnumBean();
    inner.setCustomEnum(WireSafeEnum.of(CustomEnum.TWO));
    inner.setSimpleEnum(WireSafeEnum.of(SimpleEnum.ONE));
    inner.setName("test");

    WireSafeEnumStoredAsJsonBean bean = new WireSafeEnumStoredAsJsonBean();
    bean.setInner(inner);

    JsonNode node = customCopy.valueToTree(bean);
    String innerJson = node.get("inner").textValue();
    JsonNode innerNode = customCopy.readTree(innerJson);

    assertThat(innerNode.get("customEnum").isNumber()).isTrue();
    assertThat(innerNode.get("customEnum").intValue()).isEqualTo(2);
  }
}
