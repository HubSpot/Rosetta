package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.JsonNode;

public interface NodeToBindValue {
  boolean wantsNode(JsonNode node);
  Object value(JsonNode node);
}
