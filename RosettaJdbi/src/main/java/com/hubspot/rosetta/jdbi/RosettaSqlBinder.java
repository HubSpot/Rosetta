package com.hubspot.rosetta.jdbi;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.skife.jdbi.v2.SQLStatement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.hubspot.rosetta.jackson.RosettaView;

public class RosettaSqlBinder {
  private final ObjectMapper mapper;
  private final ImmutableList<NodeToBindValue> ntbvList;

  RosettaSqlBinder(ObjectMapper mapper, ImmutableList<NodeToBindValue> ntbvList) {
    this.mapper = mapper;
    this.ntbvList = ntbvList;
  }

  public void bindAll(SQLStatement<?> q, Object obj) {
    boolean useTextValue;
    Map.Entry<String, JsonNode> node;
    final JsonNode objNode = objectToTree(obj);
    final Iterator<Map.Entry<String, JsonNode>> it = objNode.fields();
    while(it.hasNext()) {
      useTextValue = true;
      node = it.next();
      for (NodeToBindValue ntbv : ntbvList) {
        if (ntbv.wantsNode(node.getValue())) {
          bindOne(q, node.getKey(), ntbv.value(node.getValue()));
          useTextValue = false;
          break;
        }
      }
      if (useTextValue) {
        bindOne(q, node.getKey(), node.getValue().asText());
      }
    }
  }

  private JsonNode objectToTree(Object obj) {
    final ObjectWriter writer = mapper.writerWithView(RosettaView.class);
    @SuppressWarnings("deprecation")  // We need to support databind 2.1 for now...
    final TokenBuffer buf = new TokenBuffer(mapper);
    final JsonParser jp = buf.asParser();
    final JsonNode objNode;
    try {
      writer.writeValue(buf, obj);
      objNode = mapper.readTree(jp);
    } catch(IOException e) {
      throw Throwables.propagate(e);
    } finally {
      try {
        jp.close();
      } catch (IOException e) {
        throw Throwables.propagate(e);
      }
    }
    return objNode;
  }

  /**
   * Sets the actual binding. SQLStatements don't play well with Mockito due to final methods, so rather than testing
   * this class with a mocked SQLStatement, we'll do it by extending this class and replacing this method.
   */
  @VisibleForTesting
  protected void bindOne(SQLStatement<?> q, String fieldName, Object value) {
    q.bind(fieldName, value);
  }

}
