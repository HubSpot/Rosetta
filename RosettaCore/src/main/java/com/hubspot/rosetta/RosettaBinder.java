package com.hubspot.rosetta;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;

import java.util.Iterator;
import java.util.Map.Entry;

public enum RosettaBinder {
  INSTANCE;

  public interface Callback {
    void bind(String key, Object value);
  }

  public void bind(String prefix, JsonNode node, Callback callback) {
    for (Iterator<Entry<String, JsonNode>> iterator = node.fields(); iterator.hasNext(); ) {
      Entry<String, JsonNode> field = iterator.next();
      String key = prefix.isEmpty() ? field.getKey() : prefix + "." + field.getKey();
      JsonNode value = field.getValue();

      if (value.isNull()) {
        callback.bind(key, null);
      } else if (value.isBoolean()) {
        callback.bind(key, value.booleanValue());
      } else if (value.isBinary()) {
        callback.bind(key, ((BinaryNode) value).binaryValue());
      } else if (value.isObject()) {
        bind(key, value, callback);
      } else {
        callback.bind(key, value.asText());
      }
    }
  }
}
