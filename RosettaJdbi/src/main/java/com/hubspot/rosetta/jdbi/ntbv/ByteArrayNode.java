package com.hubspot.rosetta.jdbi.ntbv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.hubspot.rosetta.jdbi.NodeToBindValue;

public class ByteArrayNode implements NodeToBindValue {

  @Override
  public boolean wantsNode(JsonNode node) {
    return node.isBinary();
  }

  @Override
  public Object value(JsonNode node) {
    return ((BinaryNode)node).binaryValue();
  }

}
