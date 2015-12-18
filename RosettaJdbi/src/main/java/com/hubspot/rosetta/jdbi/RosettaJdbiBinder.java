package com.hubspot.rosetta.jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.RosettaBinder;
import com.hubspot.rosetta.RosettaBinder.Callback;
import org.skife.jdbi.v2.SQLStatement;

import com.fasterxml.jackson.databind.JsonNode;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.tweak.Argument;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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

    RosettaBinder.INSTANCE.bind(prefix, node, new Callback() {

      @Override
      public void bind(String key, final Object value) {
        if (value instanceof List) {
          q.bind(key, new Argument() {

            @Override
            public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
              // TODO don't hardcode VARCHAR
              Array array = ctx.getConnection().createArrayOf("VARCHAR", ((List<?>) value).toArray());
              statement.setArray(position, array);
            }
          });
        } else{
          q.bind(key, value);
        }
      }
    });
  }
}
