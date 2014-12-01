package com.hubspot.rosetta.jdbi;

import com.hubspot.rosetta.RosettaBinder;
import com.hubspot.rosetta.RosettaBinder.Callback;
import org.skife.jdbi.v2.SQLStatement;

import com.fasterxml.jackson.databind.JsonNode;
import com.hubspot.rosetta.Rosetta;
import org.skife.jdbi.v2.sqlobject.Binder;

public enum RosettaJdbiBinder implements Binder<BindWithRosetta, Object> {
  INSTANCE;

  @Override
  public void bind(final SQLStatement<?> q, BindWithRosetta bind, Object arg) {
    JsonNode node = Rosetta.getMapper().valueToTree(arg);
    String prefix = bind.value();
    RosettaBinder.INSTANCE.bind(prefix, node, new Callback() {

      @Override
      public void bind(String key, Object value) {
        q.bind(key, value);
      }
    });
  }
}
