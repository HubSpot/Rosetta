package com.hubspot.rosetta.jdbi3;

import java.util.HashMap;
import java.util.Map;

import org.jdbi.v3.core.statement.SqlStatement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.RosettaBinder;

public class RosettaStatementBinder {

  @FunctionalInterface
  public interface BindingBuilder {
    StatementApplyer bind(Object arg);
  }

  @FunctionalInterface
  public interface StatementApplyer {
    void onStatement(SqlStatement<?> statement);
  }

  public static BindingBuilder withPrefix(String prefix) {
    if (prefix == null) {
      throw new NullPointerException("Prefix must not be null");
    }

    return arg -> bind(prefix, arg);
  }

  public static StatementApplyer bind(Object arg) {
    return bind("", arg);
  }

  private static StatementApplyer bind(String initialPrefix, Object arg) {
    return statement -> {
      String prefix = initialPrefix;

      ObjectMapper objectMapper = statement.getConfig(RosettaObjectMapper.class).getObjectMapper();

      JsonNode node = objectMapper.valueToTree(arg);

      if (node.isValueNode() || node.isArray()) {
        node = objectMapper.createObjectNode().set(prefix.isEmpty() ? "it" : prefix, node);
        prefix = "";
      }

      Map<String, Object> namedValues = new HashMap<>();
      RosettaBinder.INSTANCE.bind(prefix, node, namedValues::put);

      statement.bindMap(namedValues);
    };
  }

  // prevent instantiation
  private RosettaStatementBinder() {
    throw new AssertionError();
  }
}
