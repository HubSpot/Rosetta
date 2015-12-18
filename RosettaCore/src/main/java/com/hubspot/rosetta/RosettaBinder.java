package com.hubspot.rosetta;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

      if (value.isObject()) {
        bind(key, value, callback);
      } else if (value.isArray()) {
        List<Object> elements = new ArrayList<>();
        for (JsonNode element : value) {
          elements.add(unwrapJsonValue(element));
        }

        callback.bind(key, elements);
      } else {
        callback.bind(key, unwrapJsonValue(value));
      }
    }
  }

  private Object unwrapJsonValue(JsonNode node) {
    if (node.isNull()) {
      return null;
    } else if (node.isBoolean()) {
      return node.booleanValue();
    } else if (node.isBinary()) {
      return ((BinaryNode) node).binaryValue();
    } else if (node.isNumber()) {
      return node.numberValue();
    } else {
      return node.asText();
    }
  }
}
