package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.RosettaBinder;
import com.hubspot.rosetta.RosettaBinder.Callback;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;

public enum RosettaJdbiBinder implements Binder<BindWithRosetta, Object> {
  INSTANCE;

  @Override
  public void bind(final SQLStatement<?> q, BindWithRosetta bind, Object arg) {
    ObjectMapper objectMapper = RosettaObjectMapperOverride.resolve(q.getContext());

    JsonNode node = objectMapper.valueToTree(arg);
    String prefix = bind.value();

    if (node.isValueNode() || node.isArray()) {
      node = objectMapper.createObjectNode().set(prefix.isEmpty() ? "it" : prefix, node);
      prefix = "";
    }

    RosettaBinder.INSTANCE.bind(
      prefix,
      node,
      new Callback() {
        @Override
        public void bind(String key, Object value) {
          q.bind(key, value);
        }
      }
    );
  }
}
