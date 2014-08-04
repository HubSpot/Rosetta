package com.hubspot.rosetta.jdbi.ntbv;

import com.fasterxml.jackson.databind.JsonNode;
import com.hubspot.rosetta.jdbi.NodeToBindValue;

public class BooleanNode implements NodeToBindValue {

  @Override
  public boolean wantsNode(JsonNode node) {
    return node.isBoolean();
  }

  @Override
  public Object value(JsonNode node) {
    return node.asBoolean();
  }

}
