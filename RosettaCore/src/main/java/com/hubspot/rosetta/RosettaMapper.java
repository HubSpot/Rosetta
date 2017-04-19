package com.hubspot.rosetta;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.internal.TableNameExtractor;

import java.lang.reflect.Type;
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
 */
public class RosettaMapper<T> {
  private static TableNameExtractor TABLE_NAME_EXTRACTOR = chooseTableNameExtractor();

  private final JavaType type;
  private final ObjectMapper objectMapper;
  private final String tableName;

  public RosettaMapper(Class<T> type) {
    this(type, Rosetta.getMapper());
  }

  public RosettaMapper(Class<T> type, ObjectMapper objectMapper) {
    this(type, objectMapper, null);
  }

  public RosettaMapper(Class<T> type, String tableName) {
    this(type, Rosetta.getMapper(), tableName);
  }

  public RosettaMapper(Class<T> type, ObjectMapper objectMapper, String tableName) {
    this((Type) type, objectMapper, tableName);
  }

  public RosettaMapper(Type type, ObjectMapper objectMapper, String tableName) {
    this.type = objectMapper.constructType(type);
    this.objectMapper = objectMapper;
    this.tableName = tableName;
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
      // calling getObject on a BLOB/CLOB produces weird results
      switch (metadata.getColumnType(i)) {
        case Types.BLOB:
          value = rs.getBytes(i);
          break;
        case Types.CLOB:
          value = rs.getString(i);
          break;
        default:
          value = rs.getObject(i);
      }

      // don't use table name extractor because we don't want aliased table name
      boolean overwrite = metadata.getTableName(i).equals(this.tableName);
      String tableName = TABLE_NAME_EXTRACTOR.getTableName(metadata, i);
      if (tableName != null && !tableName.isEmpty()) {
        String qualifiedName = tableName + "." + metadata.getColumnName(i);
        add(map, qualifiedName, value, overwrite);
      }

      add(map, label, value, overwrite);
    }

    return objectMapper.convertValue(map, type);
  }

  private void add(Map<String, Object> map, String label, Object value, boolean overwrite) {
    if (label.contains(".")) {
      int periodIndex = label.indexOf('.');
      String prefix = label.substring(0, periodIndex);
      String suffix = label.substring(periodIndex + 1);

      @SuppressWarnings("unchecked")
      Map<String, Object> submap = (Map<String, Object>) map.get(prefix);
      if (submap == null) {
        submap = new HashMap<>();
        map.put(prefix, submap);
      }

      add(submap, suffix, value, overwrite);
    } else {
      if (overwrite || !map.containsKey(label)) {
        map.put(label, value);
      }
    }
  }

  private static TableNameExtractor chooseTableNameExtractor() {
    try {
      Class.forName("com.mysql.jdbc.ResultSetMetaData");
      return (TableNameExtractor) Class.forName("com.mysql.jdbc.MysqlTableNameExtractor").newInstance();
    } catch (Exception e) {
      return new TableNameExtractor.Default();
    }
  }
}
