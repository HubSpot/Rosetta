package com.hubspot.rosetta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * Provides a fluent API to customized Node parsing.
 *
 * The current goal of NodeMapper is to map one or more JsonNodes to a T instance while ignoring and/or providing
 * access to other, non-T-related nodes.
 *
 * Consider the following Class:
 *
 * <pre>
 * {@code
 * class User {
 *   String name;
 *   // ...
 * }
 * }
 * </pre>
 *
 * Let's say you have a JSON node like {@code { "name": "Bob", "isLoggedIn": true }}. You may need to know if the user
 * is logged in, but would prefer it not be part of the actual class. This is often the case when implementing HTTP APIs
 * that may require more context than your project's data structures want to represent. Here's how you'd handle this:
 *
 * <pre>
 * {@code
 * RosettaNodeMapper nodeMapper = RosettaNodeMapper.newBuilder.addNodes(myJsonNode).extractKeys("isLoggedIn").build();
 * MyObject o = mapper.map(MyObject.class);
 * AnotherObject oo = mapper.map(AnotherObject.class) // You can even map multiple types from the same node(s)!
 * Optional<JsonNode> loggedIn = mapper.getExtractedValue("isLoggedIn");
 * }
 * </pre>
 *
 *
 * @param nodes
 * @return
 */
public class RosettaNodeMapper {
  private final Map<String, Optional<JsonNode>> extractedValues;
  private ObjectNode node;

  public static RosettaNodeMapper.Builder newBuilder() {
    return new Builder();
  }

  protected RosettaNodeMapper(ObjectNode node, Map<String, Optional<JsonNode>> extractedValues) {
    this.node = node;
    this.extractedValues = extractedValues;
  }

  public <T> T map(Class<T> klass) {
    return RosettaMapperFactory.forType(klass).mapNode(node);
  }

  public Map<String, Optional<JsonNode>> getExtractedValues() {
    return extractedValues;
  }

  public Optional<JsonNode> getExtractedValue(String keyName) {
    return extractedValues.get(keyName);
  }

  public static class Builder {
    private final List<JsonNode> nodes;
    private final List<String> extract;

    private Builder() {
      this.nodes = new ArrayList<JsonNode>();
      this.extract = new ArrayList<String>();
    }

    public Builder addNodes(JsonNode... nodes) {
      Collections.addAll(this.nodes, nodes);
      return this;
    }

    public Builder extractKeys(String... keyNames) {
      Collections.addAll(this.extract, keyNames);
      return this;
    }

    public RosettaNodeMapper build() {
      Preconditions.checkArgument(nodes.size() > 0, "No nodes supplied.");
      Preconditions.checkArgument(nodes.get(0).isObject(), "Node is not an object!");

      ObjectNode node = join();
      return new RosettaNodeMapper(node, extract(node));
    }

    private Map<String, Optional<JsonNode>> extract(ObjectNode node) {
      final Map<String, Optional<JsonNode>> extractedValues = Maps.newHashMapWithExpectedSize(extract.size());
      JsonNode extracted;

      for (final String name : extract) {
        extracted = node.remove(name);
        extractedValues.put(name, Optional.fromNullable(extracted));
      }
      return extractedValues;
    }

    private ObjectNode join() {
      ObjectNode finalNode = (ObjectNode) nodes.get(0);
      for(int x=1; x<nodes.size(); x++) {
        Preconditions.checkArgument(nodes.get(x).isObject(), "Node is not an object!");
        finalNode.setAll((ObjectNode)nodes.get(x));
      }
      nodes.clear();
      return finalNode;
    }
  }
}
