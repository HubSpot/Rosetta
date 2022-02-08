package com.hubspot.rosetta.immutables;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
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

    assertThat(serialize(bean)).isEqualTo(asNode("{\"simple\": \"ONE\", \"custom\": 1, \"simpleMaybe\": \"TWO\", \"customMaybe\": 2}"));
  }

  @Test
  public void itCanDeserializeBeanWithWireSafeField() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimple(WireSafeEnum.of(SimpleEnum.ONE));
    bean.setSimpleMaybe(Optional.of(WireSafeEnum.of(SimpleEnum.TWO)));
    bean.setCustom(WireSafeEnum.of(CustomEnum.TWO));
    bean.setCustomMaybe(Optional.of(WireSafeEnum.of(CustomEnum.ONE)));

    assertThat(deserialize("{\"simple\": \"ONE\", \"simpleMaybe\": \"TWO\", \"custom\": 2, \"customMaybe\": 1}", WireSafeBean.class)).isEqualTo(bean);
  }

  @Test
  public void itCanSerializeOptionalEmptyWireSafeFields() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimple(WireSafeEnum.of(SimpleEnum.ONE));
    bean.setCustom(WireSafeEnum.of(CustomEnum.ONE));

    assertThat(serialize(bean)).isEqualTo(asNode("{\"simple\": \"ONE\", \"custom\": 1, \"simpleMaybe\": null, \"customMaybe\": null}"));
  }

  @Test
  public void itCanDeserializeBeanWithEmptyWireSafeField() {
    WireSafeBean bean = new WireSafeBean();
    bean.setSimple(WireSafeEnum.of(SimpleEnum.ONE));
    bean.setSimpleMaybe(Optional.empty());
    bean.setCustom(WireSafeEnum.of(CustomEnum.TWO));

    assertThat(deserialize("{\"simple\": \"ONE\", \"simpleMaybe\": null, \"custom\": 2 }", WireSafeBean.class)).isEqualTo(bean);
  }

  private JsonNode asNode(String value) {
    return deserialize(value, JsonNode.class);
  }

  private <T> T deserialize(String value, Class<T> klass) {
    try {
      return MAPPER.readValue(value, klass);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private JsonNode serialize(Object o) {
    return MAPPER.valueToTree(o);
  }
}
