package com.hubspot.rosetta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.jackson.RosettaView;

/**
 * Core mapping functionality.
 *
 * The {@code map} methods here take iterable result sets (field names * to String values) and use Jackson to bind
 * either those values or * custom values (via {@link CustomMappedValue}) to a * <code>type</code> instance.
 *
 * While mapping a {@link ResultSet} is the classic example, any arbitrary mapping of fields and values may be
 * translated to an instance of <code>type</code>.
 *
 * @see #map(ResultSet)
 * @author tdavis
 */
public class RosettaMapper<T> {

  private final Tablet tablet;
  private final Class<T> type;
  private final ObjectMapper mapper;

  public RosettaMapper(Class<T> type, ObjectMapper mapper) {
    this.mapper = mapper;
    this.tablet = Rosetta.tabletForType(type);
    this.type = tablet.getUnwrappedType(type);

    tablet.useView(RosettaView.class);
  }

  /**
   * Map an entire ResultSet (one or more rows) to T instance(s).
   *
   * @throws SQLException
   */
  public List<T> map(final ResultSet rs) throws SQLException {
    final List<T> results = new ArrayList<T>(50);
    while (rs.next()) {
      results.add(this.mapRow(rs));
    }
    return results;
  }

  /**
   * Map a single ResultSet row to a T instance.
   *
   * @throws SQLException
   */
  public T mapRow(final ResultSet rs) throws SQLException {
    final Map<String, Object> propertyMap = new HashMap<String, Object>();
    final ResultSetMetaData metadata = rs.getMetaData();
    String name;
    Object value;
    for (int i = 1; i <= metadata.getColumnCount(); ++i) {
      name = metadata.getColumnLabel(i);
      // calling getObject on a CLOB produces really weird results
      if (Types.CLOB == metadata.getColumnType(i)) {
        value = rs.getString(i);
      } else {
        value = rs.getObject(i);
      }
      if (value != null) {
        propertyMap.put(name, value);
      }
    }
    return tablet.finalizeInstance(mapper.convertValue(propertyMap, type));
  }

  /**
   * Covert a generic map of name/value pairs to a T instance.
   *
   */
  public T map(final Map<String, ?> rs) {
    return tablet.finalizeInstance(mapper.convertValue(rs, type));
  }

  /**
   * Covert a generic map of name/value pairs to a T instance.
   *
   */
  public T map(final String s) {
    try {
      return tablet.finalizeInstance(mapper.readValue(s, type));
    } catch(IOException e) {
      // It's a String...
      return null;
    }
  }

  /**
   * Covert a JsonNode to a T instance.
   *
   */
  public T mapNode(final JsonNode node) {
    return tablet.finalizeInstance(mapper.convertValue(node, type));
  }

  /**
   * Proxy to ObjectMapper.
   *
   * @throws JsonProcessingException
   */
  public String writeValueAsString(T obj) throws JsonProcessingException {
    return mapper.writeValueAsString(obj);
  }

  @Override
  public int hashCode() {
    return type.hashCode();
  }

}
