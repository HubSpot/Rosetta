package com.hubspot.rosetta.immutables;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hubspot.immutables.utils.WireSafeEnum;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.immutables.beans.CustomEnum;
import com.hubspot.rosetta.immutables.beans.SimpleEnum;
import com.hubspot.rosetta.immutables.beans.WireSafeBean;

public class WireSafeEnumTest {
  private static final ObjectMapper MAPPER = Rosetta
      .getMapper()
      .copy()
      .registerModules(new RosettaImmutablesModule(), new Jdk8Module());

  @Test
  public void itCanSerializeBeanWithWireSafeField() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimple(WireSafeEnum.of(SimpleEnum.ONE));
    bean.setCustom(WireSafeEnum.of(CustomEnum.ONE));
    bean.setSimpleMaybe(Optional.of(WireSafeEnum.of(SimpleEnum.TWO)));
    bean.setCustomMaybe(Optional.of(WireSafeEnum.of(CustomEnum.TWO)));
    bean.setSimpleList(
        ImmutableList.of(WireSafeEnum.of(SimpleEnum.ONE), WireSafeEnum.of(SimpleEnum.TWO))
    );
    bean.setSimpleMap(
        ImmutableMap.of(
            "A", WireSafeEnum.of(SimpleEnum.ONE),
            "B", WireSafeEnum.of(SimpleEnum.TWO)
        )
    );
    bean.setCustomList(
        ImmutableList.of(WireSafeEnum.of(CustomEnum.ONE), WireSafeEnum.of(CustomEnum.TWO))
    );
    bean.setCustomMap(
        ImmutableMap.of(
            "A", WireSafeEnum.of(CustomEnum.ONE),
            "B", WireSafeEnum.of(CustomEnum.TWO)
        )
    );

    assertThat(serialize(bean))
        .isEqualTo(
            objectNode(
                ImmutableMap.
                <String, JsonNode>builder()
                .put("simple", new TextNode("ONE"))
                .put("custom", new IntNode(1))
                .put("simpleMaybe", new TextNode("TWO"))
                .put("simpleList", arrayNode(new TextNode("ONE"), new TextNode("TWO")))
                .put("simpleMap", objectNode(ImmutableMap.of("A", new TextNode("ONE"), "B", new TextNode("TWO"))))
                .put("customMaybe", new IntNode(2))
                .put("customList", arrayNode(new IntNode(1), new IntNode(2)))
                .put("customMap", objectNode(ImmutableMap.of("A", new IntNode(1), "B", new IntNode(2))))
                .build()
            )
        );
  }

  @Test
  public void itCanDeserializeBeanWithWireSafeField() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimple(WireSafeEnum.of(SimpleEnum.ONE));
    bean.setSimpleMaybe(Optional.of(WireSafeEnum.of(SimpleEnum.TWO)));
    bean.setSimpleList(
        ImmutableList.of(WireSafeEnum.of(SimpleEnum.ONE), WireSafeEnum.of(SimpleEnum.TWO))
    );
    bean.setSimpleMap(
        ImmutableMap.of(
            "A", WireSafeEnum.of(SimpleEnum.ONE),
            "B", WireSafeEnum.of(SimpleEnum.TWO)
        )
    );
    bean.setCustom(WireSafeEnum.of(CustomEnum.TWO));
    bean.setCustomMaybe(Optional.of(WireSafeEnum.of(CustomEnum.ONE)));
    bean.setCustomList(
        ImmutableList.of(WireSafeEnum.of(CustomEnum.ONE), WireSafeEnum.of(CustomEnum.TWO))
    );
    bean.setCustomMap(
        ImmutableMap.of(
            "A", WireSafeEnum.of(CustomEnum.ONE),
            "B", WireSafeEnum.of(CustomEnum.TWO)
        )
    );

    assertThat(
        convert(
          objectNode(
            ImmutableMap.
                <String, JsonNode>builder()
                .put("simple", new TextNode("ONE"))
                .put("custom", new IntNode(2))
                .put("simpleMaybe", new TextNode("TWO"))
                .put("simpleList", arrayNode(new TextNode("ONE"), new TextNode("TWO")))
                .put("simpleMap", objectNode(ImmutableMap.of("A", new TextNode("ONE"), "B", new TextNode("TWO"))))
                .put("customMaybe", new IntNode(1))
                .put("customList", arrayNode(new IntNode(1), new IntNode(2)))
                .put("customMap", objectNode(ImmutableMap.of("A", new IntNode(1), "B", new IntNode(2))))
                .build()
          ), WireSafeBean.class
        )
    )
        .isEqualTo(bean);
  }

  @Test
  public void itCanSerializeOptionalEmptyWireSafeFields() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimple(WireSafeEnum.of(SimpleEnum.ONE));
    bean.setCustom(WireSafeEnum.of(CustomEnum.ONE));

    assertThat(serialize(bean))
        .isEqualTo(
            objectNode(
                ImmutableMap.
                    <String, JsonNode>builder()
                    .put("simple", new TextNode("ONE"))
                    .put("custom", new IntNode(1))
                    .put("simpleMaybe", NullNode.getInstance())
                    .put("simpleList", NullNode.getInstance())
                    .put("simpleMap", NullNode.getInstance())
                    .put("customMaybe", NullNode.getInstance())
                    .put("customList", NullNode.getInstance())
                    .put("customMap", NullNode.getInstance())
                    .build()
            )
        );
  }

  @Test
  public void itCanSerializeEmptyListWireSafeFields() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimpleList(Collections.emptyList());
    bean.setCustomList(Collections.emptyList());

    assertThat(serialize(bean))
        .isEqualTo(
            objectNode(
                ImmutableMap.
                    <String, JsonNode>builder()
                    .put("simple", NullNode.getInstance())
                    .put("custom", NullNode.getInstance())
                    .put("simpleMaybe", NullNode.getInstance())
                    .put("simpleList", arrayNode())
                    .put("simpleMap", NullNode.getInstance())
                    .put("customMaybe", NullNode.getInstance())
                    .put("customList", arrayNode())
                    .put("customMap", NullNode.getInstance())
                    .build()
            )
        );
  }

  @Test
  public void itCanSerializeEmptyMapWireSafeFields() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimpleMap(ImmutableMap.of());
    bean.setCustomMap(ImmutableMap.of());

    assertThat(serialize(bean))
        .isEqualTo(
            objectNode(
                ImmutableMap.
                    <String, JsonNode>builder()
                    .put("simple", NullNode.getInstance())
                    .put("custom", NullNode.getInstance())
                    .put("simpleMaybe", NullNode.getInstance())
                    .put("simpleList", NullNode.getInstance())
                    .put("simpleMap", objectNode(ImmutableMap.of()))
                    .put("customMaybe", NullNode.getInstance())
                    .put("customList", NullNode.getInstance())
                    .put("customMap", objectNode(ImmutableMap.of()))
                    .build()
            )
        );
  }

  @Test
  public void itCanDeserializeBeanWithEmptyWireSafeField() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimple(WireSafeEnum.of(SimpleEnum.ONE));
    bean.setSimpleMaybe(Optional.empty());
    bean.setCustom(WireSafeEnum.of(CustomEnum.TWO));

    assertThat(
        convert(
            objectNode(
                ImmutableMap.
                    <String, JsonNode>builder()
                    .put("simple", new TextNode("ONE"))
                    .put("custom", new IntNode(2))
                    .put("simpleMaybe", NullNode.getInstance())
                    .build()
            ), WireSafeBean.class
        )
    )
        .isEqualTo(bean);
  }

  private JsonNode serialize(Object o) {
    return MAPPER.valueToTree(o);
  }

  private static <T> T convert(JsonNode json, Class<T> klass) {
    try {
      return MAPPER.treeToValue(json, klass);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private static JsonNode objectNode(Map<String, JsonNode> fields) {
    return JsonNodeFactory.instance.objectNode().setAll(fields);
  }

  private static JsonNode arrayNode(JsonNode... elements) {
    return JsonNodeFactory.instance.arrayNode().addAll(Arrays.asList(elements));
  }
}
