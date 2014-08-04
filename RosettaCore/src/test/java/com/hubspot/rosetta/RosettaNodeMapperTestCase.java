package com.hubspot.rosetta;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;

public class RosettaNodeMapperTestCase {

  private static ObjectNode[] nodes;

  @BeforeClass
  public static void setUp() {
    ObjectNode node1 = JsonNodeFactory.instance.objectNode();
    node1.put("extra", true);

    ObjectNode node2 = JsonNodeFactory.instance.objectNode();
    node2.put("pojo1", 1);
    ObjectNode node3 = JsonNodeFactory.instance.objectNode();
    node3.put("pojo2", 2);

    nodes = new ObjectNode[]{node1, node2, node3};
  }

  @Test
  public void simple() {
    RosettaNodeMapper mapper = RosettaNodeMapper.newBuilder().addNodes(nodes).extractKeys("extra", "missing").build();

    Pojo1 p1 = mapper.map(Pojo1.class);
    Pojo2 p2 = mapper.map(Pojo2.class);
    Optional<JsonNode> extra = mapper.getExtractedValue("extra");
    Optional<JsonNode> missing = mapper.getExtractedValue("missing");

    assertThat(p1.getPojo1()).isEqualTo(1);
    assertThat(p2.getPojo2()).isEqualTo(2);
    assertThat(extra.isPresent()).isTrue();
    assertThat(extra.get().asBoolean()).isTrue();
    assertThat(missing.isPresent()).isFalse();
  }

  public static class Pojo1 {
    private int pojo1;

    public int getPojo1() {
      return pojo1;
    }

    public void setPojo1(int pojo1) {
      this.pojo1 = pojo1;
    }
  }

  public static class Pojo2 {
    private int pojo2;

    public int getPojo2() {
      return pojo2;
    }

    public void setPojo2(int pojo2) {
      this.pojo2 = pojo2;
    }
  }
}
