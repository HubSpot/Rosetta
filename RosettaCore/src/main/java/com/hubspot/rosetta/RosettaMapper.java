package com.hubspot.rosetta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Core mapping functionality.
 *
 * The {@code map} methods here take iterable result sets (field names * to String values) and use Jackson to bind
 * those values to a * <code>type</code> instance.
 *
 * @author tdavis
 */
public class RosettaMapper<T> {
  private final Class<T> type;

  public RosettaMapper(Class<T> type) {
    this.type = type;
  }

  /**
   * Map a single ResultSet row to a T instance.
   *
   * @throws SQLException
   */
  public T mapRow(ResultSet rs) throws SQLException {
    Map<String, Object> map = new HashMap<String, Object>();
    ResultSetMetaData metadata = rs.getMetaData();

    for (int i = 1; i <= metadata.getColumnCount(); ++i) {
      String label = metadata.getColumnLabel(i);
      final Object value;

      // calling getObject on a CLOB produces really weird results
      if (Types.CLOB == metadata.getColumnType(i)) {
        value = rs.getString(i);
      } else {
        value = rs.getObject(i);
      }

      if (value != null) {
        add(map, label, value);
      }
    }

    return Rosetta.getMapper().convertValue(map, type);
  }

  private void add(Map<String, Object> map, String label, Object value) {
    if (label.contains(".")) {
      int periodIndex = label.indexOf('.');
      String prefix = label.substring(0, periodIndex);
      String suffix = label.substring(periodIndex + 1);

      @SuppressWarnings("unchecked")
      Map<String, Object> submap = (Map<String, Object>) map.get(prefix);
      if (submap == null) {
        submap = new HashMap<String, Object>();
        map.put(prefix, submap);
      }

      add(submap, suffix, value);
    } else {
      map.put(label, value);
    }
  }
}
