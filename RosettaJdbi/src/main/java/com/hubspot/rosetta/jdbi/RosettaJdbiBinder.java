package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.RosettaBinder;
import com.hubspot.rosetta.RosettaBinder.Callback;
import org.skife.jdbi.v2.SQLStatement;

import com.fasterxml.jackson.databind.JsonNode;
import org.skife.jdbi.v2.sqlobject.Binder;

import java.lang.annotation.Annotation;

public enum RosettaJdbiBinder implements Binder<BindWithRosetta, Object> {
  INSTANCE;

  @Override
  public void bind(final SQLStatement<?> q, BindWithRosetta bind, Object arg) {
    ObjectMapper objectMapper = RosettaObjectMapperOverride.resolveStatementBinder(q.getContext());

    JsonNode node = objectMapper.valueToTree(arg);
    String prefix = bind.value();

    if (node.isValueNode() || node.isArray()) {
      node = objectMapper.createObjectNode().set(prefix.isEmpty() ? "it" : prefix, node);
      prefix = "";
    }

    RosettaBinder.INSTANCE.bind(prefix, node, new Callback() {

      @Override
      public void bind(String key, Object value) {
        q.bind(key, value);
      }
    });
  }

  public void bind(final SQLStatement<?> q, final String prefix, Object arg) {

    RosettaJdbiBinder.INSTANCE.bind(q, new BindWithRosetta() {

      @Override
      public Class<? extends Annotation> annotationType() {
        return BindWithRosetta.class;
      }

      @Override
      public String value()  {
        if (prefix != null) {
          return prefix;
        }

        return "";
      }

    }, arg);


  }

  public void bind(final SQLStatement q,Object arg) {
    bind(q,"",arg);
  }

}
